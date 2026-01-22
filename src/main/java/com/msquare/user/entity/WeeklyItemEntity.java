package com.msquare.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeeklyItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID weeklyItemId;

    private UUID itemId;
    private Integer weekNumber;
    private UUID shopId;
}
