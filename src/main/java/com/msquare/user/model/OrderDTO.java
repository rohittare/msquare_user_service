package com.msquare.user.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private UUID orderId;
    private String status;
    private String paymentType;
    private Double totalAmount;
    private LocalDateTime orderDate;

    private List<OrderItemDTO> items;
}
