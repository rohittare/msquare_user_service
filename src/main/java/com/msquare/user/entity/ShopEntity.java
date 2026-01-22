package com.msquare.user.entity;

import com.msquare.user.entity.ItemEntity;
import com.msquare.user.entity.AddressEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shop")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shopId;

    @Column(nullable = false)
    private String name;

    private String description;

    private Double rating;
    private String tags;
    private boolean pureVeg;
    private boolean isApproved;

    @Column(columnDefinition = "TEXT")
    private String picture;

    /* Address ID only */
    private UUID addressId;
}
