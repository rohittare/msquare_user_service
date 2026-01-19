package com.msquare.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "ref_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class userEntity {
    @Id
    private Long id;
    private String username;
}
