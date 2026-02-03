package com.msquare.user.repo;
import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.msquare.user.entity.UserDetailEntity;

@Repository
public interface   UserDetailRepo extends JpaRepository<UserDetailEntity , UUID>{
    Optional<UserDetailEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
