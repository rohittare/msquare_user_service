package com.msquare.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthResponse {
    private String token;
    private String tokenType;
    private UUID userId;
    private UUID shopId;
    private String email;
    private String role;
}
