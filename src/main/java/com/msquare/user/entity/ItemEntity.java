package com.msquare.user.entity;


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

    private UUID shopId;

    @Column(nullable = false)
    private String name; 

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price;

    private String category; // [extra , regular , none]

    private boolean veg;

    private boolean available;

    private boolean special; 

}
