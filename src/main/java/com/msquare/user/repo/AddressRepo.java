package com.msquare.user.repo;

import java.util.UUID;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.msquare.user.entity.AddressEntity;

@Repository
public interface AddressRepo extends JpaRepository<AddressEntity , UUID>{
    List<AddressEntity> findByUserId(UUID userId);
}