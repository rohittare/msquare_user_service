package com.msquare.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private UUID orderItemId;
    private UUID itemId;
    private String itemName;
    private Integer quantity;
    private Double priceAtOrderTime;
}
