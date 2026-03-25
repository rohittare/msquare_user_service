package com.msquare.user.service;

import com.msquare.user.common.OrderStatus;
import com.msquare.user.common.PaymentType;
import com.msquare.user.entity.ItemEntity;
import com.msquare.user.entity.OrderEntity;
import com.msquare.user.entity.OrderItemEntity;
import com.msquare.user.entity.UserDetailEntity;
import com.msquare.user.model.CreateOrderRequest;
import com.msquare.user.model.OrderItemRequest;
import com.msquare.user.model.OrderItemResponse;
import com.msquare.user.model.OrderResponse;
import com.msquare.user.model.UserDetailDTO;
import com.msquare.user.model.UpdateOrderStatusRequest;
import com.msquare.user.repo.ItemRepo;
import com.msquare.user.repo.OrderRepo;
import com.msquare.user.repo.ShopRepo;
import com.msquare.user.repo.UserDetailRepo;
import com.msquare.user.security.ShopPrincipal;
import com.msquare.user.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final ItemRepo itemRepo;
    private final ShopRepo shopRepo;
    private final UserDetailRepo userDetailRepo;
    private final AddressService addressService;

    @Autowired
    public OrderService(
            OrderRepo orderRepo,
            ItemRepo itemRepo,
            ShopRepo shopRepo,
            UserDetailRepo userDetailRepo,
            AddressService addressService
    ) {
        this.orderRepo = orderRepo;
        this.itemRepo = itemRepo;
        this.shopRepo = shopRepo;
        this.userDetailRepo = userDetailRepo;
        this.addressService = addressService;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order request is required");
        }

        UUID userId = requireUserId();

        if (request.getShopId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shop ID is required");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order items are required");
        }

        UserDetailEntity user = userDetailRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        shopRepo.findById(request.getShopId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));

        OrderEntity order = new OrderEntity();
        order.setStatus(OrderStatus.PLACED);
        order.setPaymentType(resolvePaymentType(request.getPaymentType()));
        order.setShopId(request.getShopId());
        order.setUser(user);

        List<OrderItemEntity> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemRequest itemRequest : request.getItems()) {
            if (itemRequest == null || itemRequest.getItemId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item ID is required");
            }
            if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than 0");
            }

            ItemEntity item = itemRepo.findById(itemRequest.getItemId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

            if (item.getShopId() == null || !item.getShopId().equals(request.getShopId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All items must belong to the same shop");
            }

            if (item.getPrice() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item price is not set");
            }

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setItem(item.getItemId());
            orderItem.setItemName(item.getName());
            orderItem.setPriceAtOrderTime(item.getPrice());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
            totalAmount += item.getPrice() * itemRequest.getQuantity();
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        OrderEntity saved = orderRepo.save(order);
        return mapOrder(saved);
    }

    public List<OrderResponse> getMyOrders() {
        UUID userId = requireUserId();
        List<OrderEntity> orders = orderRepo.findByUserIdWithItems(userId);
        return mapOrders(orders);
    }

    public OrderResponse getOrderDetails(UUID orderId) {
        if (orderId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order ID is required");
        }
        UUID userId = requireUserId();

        OrderEntity order = orderRepo.findByOrderIdWithItems(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (order.getUser() == null || order.getUser().getUserId() == null
                || !order.getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return mapOrder(order);
    }

    public List<OrderResponse> getShopOrders() {
        UUID shopId = requireShopId();
        List<OrderEntity> orders = orderRepo.findByShopIdWithItems(shopId);
        return mapOrders(orders);
    }

    public OrderResponse acceptOrder(UUID orderId) {
        return updateOrderStatusForShop(orderId, OrderStatus.ACCEPTED);
    }

    public OrderResponse updateOrderStatus(UUID orderId, UpdateOrderStatusRequest request) {
        if (request == null || request.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is required");
        }

        OrderStatus target = request.getStatus();

        return updateOrderStatusForShop(orderId, target);
    }

    public OrderResponse cancelOrder(UUID orderId) {
        return updateOrderStatusForUser(orderId, OrderStatus.CANCELLED);
    }

    public OrderResponse rejectOrder(UUID orderId) {
        return updateOrderStatusForShop(orderId, OrderStatus.REJECTED);
    }

    private OrderResponse updateOrderStatusForShop(UUID orderId, OrderStatus targetStatus) {
        if (orderId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order ID is required");
        }
        UUID shopId = requireShopId();

        OrderEntity order = orderRepo.findByOrderIdWithItems(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (order.getShopId() == null || !order.getShopId().equals(shopId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }


        order.setStatus(targetStatus);
        OrderEntity saved = orderRepo.save(order);
        return mapOrder(saved);
    }

    private OrderResponse updateOrderStatusForUser(UUID orderId, OrderStatus targetStatus) {
        if (orderId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order ID is required");
        }
        UUID userId = requireUserId();

        OrderEntity order = orderRepo.findByOrderIdWithItems(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (order.getUser() == null || order.getUser().getUserId() == null
                || !order.getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }


        order.setStatus(targetStatus);
        OrderEntity saved = orderRepo.save(order);
        return mapOrder(saved);
    }

    private void validateTransition(OrderStatus current, OrderStatus target) {
        if (current == null || target == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order status");
        }
        if (current == OrderStatus.PLACED) {
            if (target == OrderStatus.ACCEPTED || target == OrderStatus.CANCELLED || target == OrderStatus.REJECTED) {
                return;
            }
        } else if (current == OrderStatus.ACCEPTED && target == OrderStatus.PREPARING) {
            return;
        } else if (current == OrderStatus.PREPARING && target == OrderStatus.READY) {
            return;
        } else if (current == OrderStatus.READY && target == OrderStatus.DELIVERED) {
            return;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status transition");
    }

    private PaymentType resolvePaymentType(PaymentType paymentType) {
        if (paymentType == null) {
            return PaymentType.COD;
        }
        return paymentType;
    }

    private UUID requireUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getUserId();
        }
        if (principal instanceof ShopPrincipal) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User access required");
        }
        if (principal instanceof String) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
    }

    private UUID requireShopId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof ShopPrincipal shopPrincipal) {
            return shopPrincipal.getShopId();
        }
        if (principal instanceof UserPrincipal) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Shop access required");
        }
        if (principal instanceof String) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
    }

    private OrderResponse mapOrder(OrderEntity order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setStatus(order.getStatus());
        response.setPaymentType(order.getPaymentType());
        response.setTotalAmount(order.getTotalAmount());
        response.setShopId(order.getShopId());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(mapOrderItems(order.getOrderItems()));
        response.setUser(mapUser(order.getUser()));
        return response;
    }

    private List<OrderItemResponse> mapOrderItems(List<OrderItemEntity> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        List<OrderItemResponse> responses = new ArrayList<>();
        for (OrderItemEntity item : items) {
            OrderItemResponse response = new OrderItemResponse();
            response.setOrderItemId(item.getOrderItemId());
            response.setItemId(item.getItem());
            response.setItemName(item.getItemName());
            response.setQuantity(item.getQuantity());
            response.setPriceAtOrderTime(item.getPriceAtOrderTime());
            responses.add(response);
        }
        return responses;
    }

    private List<OrderResponse> mapOrders(List<OrderEntity> orders) {
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }
        List<OrderResponse> responses = new ArrayList<>();
        for (OrderEntity order : orders) {
            responses.add(mapOrder(order));
        }
        return responses;
    }

    private UserDetailDTO mapUser(UserDetailEntity user) {
        if (user == null) {
            return null;
        }
        UserDetailDTO dto = new UserDetailDTO();
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setUserProfilePicture(user.getUserProfilePicture());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setPassword(null);
        if (user.getUserId() != null) {
            dto.setAddresses(addressService.getAddressesByUserId(user.getUserId()));
        }
        return dto;
    }
}
