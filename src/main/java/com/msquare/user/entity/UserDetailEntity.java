package com.msquare.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_detail")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDetailEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(nullable = false)
    private String fullName;

    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;
    
    @Column(columnDefinition = "TEXT")
    private String userProfilePicture;

    private String role;

    private LocalDateTime createdDate;
    private boolean active;

}
