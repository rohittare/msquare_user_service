package com.msquare.user.model;

import com.msquare.user.common.OrderStatus;
import com.msquare.user.common.PaymentType;
import com.msquare.user.entity.UserDetailEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private UUID orderId;
    private OrderStatus status;
    private PaymentType paymentType;
    private Double totalAmount;
    private UUID shopId;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
    private UserDetailDTO user;
}
