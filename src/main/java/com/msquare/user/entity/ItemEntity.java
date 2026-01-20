package com.msquare.user.entity;


import com.msquare.user.entity.ShopEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID itemId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price;

    private String category;

    private boolean isVeg;

    private boolean isAvailable;

    private boolean isSpecial; // optional flag

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private ShopEntity shop;
}
