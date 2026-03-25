package com.msquare.user.repo;

import com.msquare.user.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity, UUID> {

    @Query("""
            SELECT DISTINCT o FROM OrderEntity o
            LEFT JOIN FETCH o.orderItems
            LEFT JOIN FETCH o.user
            WHERE o.user.userId = :userId
            ORDER BY o.createdAt DESC
            """)
    List<OrderEntity> findByUserIdWithItems(UUID userId);

    @Query("""
            SELECT DISTINCT o FROM OrderEntity o
            LEFT JOIN FETCH o.orderItems
            LEFT JOIN FETCH o.user
            WHERE o.shopId = :shopId
            ORDER BY o.createdAt DESC
            """)
    List<OrderEntity> findByShopIdWithItems(UUID shopId);

    @Query("""
            SELECT o FROM OrderEntity o
            LEFT JOIN FETCH o.orderItems
            LEFT JOIN FETCH o.user
            WHERE o.orderId = :orderId
            """)
    Optional<OrderEntity> findByOrderIdWithItems(UUID orderId);
}
