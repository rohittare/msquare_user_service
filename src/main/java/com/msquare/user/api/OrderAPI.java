package com.msquare.user.api;

import com.msquare.user.model.CreateOrderRequest;
import com.msquare.user.model.OrderResponse;
import com.msquare.user.model.UpdateOrderStatusRequest;
import com.msquare.user.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
public class OrderAPI {
    @Autowired
    protected OrderService orderService;

    @PostMapping("/orders")
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/orders/my")
    public List<OrderResponse> getMyOrders() {
        return orderService.getMyOrders();
    }

    @GetMapping("/orders/{orderId}")
    public OrderResponse getOrderDetails(@PathVariable UUID orderId) {
        return orderService.getOrderDetails(orderId);
    }

    @GetMapping("/owner/orders")
    public List<OrderResponse> getShopOrders() {
        return orderService.getShopOrders();
    }

    @PatchMapping("/orders/{orderId}/accept")
    public OrderResponse acceptOrder(@PathVariable UUID orderId) {
        return orderService.acceptOrder(orderId);
    }

    //TODO:
    @PatchMapping("/orders/{orderId}/status")
    public OrderResponse updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestBody UpdateOrderStatusRequest request
    ) {
        return orderService.updateOrderStatus(orderId, request);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    public OrderResponse cancelOrder(@PathVariable UUID orderId) {
        return orderService.cancelOrder(orderId);
    }

    @PatchMapping("/orders/{orderId}/reject")
    public OrderResponse rejectOrder(@PathVariable UUID orderId) {
        return orderService.rejectOrder(orderId);
    }
}
