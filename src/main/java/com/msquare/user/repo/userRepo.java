package com.msquare.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.msquare.user.entity.userEntity;


@Repository
public interface userRepo extends JpaRepository<userEntity, Long> {
    userEntity findByUsername(String username);
}
