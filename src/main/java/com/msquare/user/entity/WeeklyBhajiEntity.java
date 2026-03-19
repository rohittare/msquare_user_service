package com.msquare.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.UUID;
import com.msquare.user.common.MealTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "weekly_bhaji")
public class WeeklyBhajiEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID weeklyBhajiId;

    private UUID shopId;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;  // MONDAY, TUESDAY ... SUNDAY

    @Enumerated(EnumType.STRING)
    private MealTime mealTime;    // LUNCH, DINNER

    private String bhajiName;
}

