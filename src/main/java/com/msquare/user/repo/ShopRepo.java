package com.msquare.user.repo;
import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.msquare.user.entity.ShopEntity;

@Repository
public interface ShopRepo extends JpaRepository<ShopEntity , UUID>{
    List<ShopEntity> findByIsApprovedTrue();
}
