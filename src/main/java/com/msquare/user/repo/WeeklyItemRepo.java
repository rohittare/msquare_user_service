package com.msquare.user.repo;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.msquare.user.entity.WeeklyItemEntity;
import java.util.Optional;

@Repository
public interface WeeklyItemRepo extends JpaRepository<WeeklyItemEntity, UUID> {
    Optional<WeeklyItemEntity> findByShopIdAndItemIdAndWeekNumber(
            UUID shopId, UUID itemId, Integer weekNumber);

    void deleteByShopIdAndItemIdAndWeekNumber(
            UUID shopId, UUID itemId, Integer weekNumber);

}
