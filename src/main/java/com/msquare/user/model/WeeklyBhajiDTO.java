package com.msquare.user.model;
import lombok.*;
import java.time.DayOfWeek;
import java.util.UUID;
import com.msquare.user.common.MealTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyBhajiDTO {
    private UUID weeklyBhajiId;
    private UUID shopId;
    private DayOfWeek dayOfWeek;
    private MealTime mealTime;
    private String bhajiName;

}