package com.msquare.user.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.msquare.user.entity.ItemEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepo extends JpaRepository<ItemEntity, UUID> {
    List<ItemEntity> findByItemId(UUID itemId);

    List<ItemEntity> findByShopId(UUID shopId);

    List<ItemEntity> findByShopIdAndSpecialTrue(UUID shopId);

    @Query("""
                SELECT i FROM ItemEntity i
                JOIN WeeklyItemEntity w
                ON i.itemId = w.itemId
                WHERE w.shopId = :shopId
            """)
    List<ItemEntity> findAllWeeklyItems(UUID shopId);

    @Query("""
                SELECT i FROM ItemEntity i
                JOIN WeeklyItemEntity w
                ON i.itemId = w.itemId
                WHERE w.shopId = :shopId AND w.weekNumber = :weekNumber
            """)
    List<ItemEntity> findWeeklyItems(UUID shopId, Integer weekNumber);


    List<ItemEntity> findByShopIdAndCategory(UUID shopId, String category);
}
