package com.msquare.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;

    private String type; // HOME, WORK

    private String area;
    private String city;
    private String pincode;

    @Column(columnDefinition = "TEXT")
    private String fullAddress; // building, flat, landmark

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDetailEntity user;
}
