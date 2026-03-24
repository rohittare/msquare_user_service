package com.msquare.user.entity;

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

    private String phoneNumber;

    private String email;

    private String password;

    private String description;

    private Double rating;
    private String tags;
    private boolean pureVeg;
    private boolean isApproved;
    private String role;

    @Column(columnDefinition = "TEXT")
    private String picture;

   
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "addressId")
    private AddressEntity address;
}
