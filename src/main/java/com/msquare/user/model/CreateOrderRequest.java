package com.msquare.user.model;

import com.msquare.user.common.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private UUID shopId;
    private List<OrderItemRequest> items;
    private PaymentType paymentType;
}
