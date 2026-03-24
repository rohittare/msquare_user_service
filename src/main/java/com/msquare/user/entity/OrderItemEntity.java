package com.msquare.user.entity;

import com.msquare.user.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "order_item")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderItemId;
    private Integer quantity;
    private Double priceAtOrderTime;

    // @Column(columnDefinition = "TEXT")
    // private String extra; // instructions

    private String itemName;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    private UUID item;
}
