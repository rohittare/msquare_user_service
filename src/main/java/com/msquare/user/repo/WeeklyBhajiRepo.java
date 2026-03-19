package com.msquare.user.repo;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.DayOfWeek;
import com.msquare.user.common.MealTime;
import com.msquare.user.entity.WeeklyBhajiEntity;
import java.util.Optional;
public interface WeeklyBhajiRepo 
    extends JpaRepository<WeeklyBhajiEntity, UUID> {

    List<WeeklyBhajiEntity> findByShopId(UUID shopId);

    Optional<WeeklyBhajiEntity> findByShopIdAndDayOfWeekAndMealTime(
        UUID shopId,
        DayOfWeek dayOfWeek,
        MealTime mealTime
    );
}