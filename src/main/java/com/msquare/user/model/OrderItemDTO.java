package com.msquare.user.model;

import lombok.*;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

    private UUID itemId;
    private String itemName;
    private Integer quantity;
    private Double priceAtOrderTime;
    private String extra;
}
