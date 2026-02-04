package com.msquare.user.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.msquare.user.entity.ShopEntity;
import com.msquare.user.entity.UserDetailEntity;
import com.msquare.user.model.OAuthRequest;
import com.msquare.user.model.OAuthResponse;
import com.msquare.user.repo.ShopRepo;
import com.msquare.user.repo.UserDetailRepo;
import com.msquare.user.security.GoogleTokenVerifier;
import com.msquare.user.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class OAuthService {

    private final GoogleTokenVerifier googleTokenVerifier;
    private final UserDetailRepo userDetailRepo;
    private final ShopRepo shopRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public OAuthService(
            GoogleTokenVerifier googleTokenVerifier,
            UserDetailRepo userDetailRepo,
            ShopRepo shopRepo,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.googleTokenVerifier = googleTokenVerifier;
        this.userDetailRepo = userDetailRepo;
        this.shopRepo = shopRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public OAuthResponse authenticate(OAuthRequest request) {
        GoogleIdToken idToken = googleTokenVerifier.verify(request.getIdToken());
        if (idToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Google token");
        }

        Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String fullName = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        String normalizedRole = normalizeRole(request.getRole());
        if ("ADMIN".equals(normalizedRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Role not allowed");
        }
        if ("SHOP".equals(normalizedRole)) {
            return handleShopLogin(email, fullName, picture);
        }
        return handleUserLogin(email, fullName, picture, normalizedRole);
    }

    private OAuthResponse handleUserLogin(String email, String fullName, String picture, String role) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is missing from Google token");
        }
        UserDetailEntity user = userDetailRepo.findByEmail(email).orElse(null);
        if (user == null) {
            user = new UserDetailEntity();
            user.setEmail(email);
            user.setFullName(fullName);
            user.setUserProfilePicture(picture);
            user.setRole(role);
            user.setActive(true);
            user.setCreatedDate(LocalDateTime.now());
            user.setPassword(null);
            user = userDetailRepo.save(user);
        } else {
            if (user.getFullName() == null || user.getFullName().isBlank()) {
                user.setFullName(fullName);
            }
            if (user.getUserProfilePicture() == null || user.getUserProfilePicture().isBlank()) {
                user.setUserProfilePicture(picture);
            }
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole(role);
            }
            user = userDetailRepo.save(user);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", normalizeRole(user.getRole()));
        String token = jwtService.generateToken(claims, user.getUserId().toString());

        return OAuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .email(user.getEmail())
                .role(normalizeRole(user.getRole()))
                .build();
    }

    private OAuthResponse handleShopLogin(String email, String fullName, String picture) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is missing from Google token");
        }
        ShopEntity shop = shopRepo.findByEmail(email).orElse(null);
        if (shop == null) {
            shop = new ShopEntity();
            shop.setEmail(email);
            shop.setName(fullName != null ? fullName : "New Shop");
            shop.setPicture(picture);
            shop.setApproved(false);
            shop.setRating(0.0);
            shop.setRole("SHOP");
            shop.setPassword(passwordEncoder.encode(generateRandomPassword()));
            shop = shopRepo.save(shop);
        } else {
            if (shop.getName() == null || shop.getName().isBlank()) {
                shop.setName(fullName != null ? fullName : shop.getName());
            }
            if (shop.getPicture() == null || shop.getPicture().isBlank()) {
                shop.setPicture(picture);
            }
            if (shop.getRole() == null || shop.getRole().isBlank()) {
                shop.setRole("SHOP");
            }
            shop = shopRepo.save(shop);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "SHOP");
        String token = jwtService.generateToken(claims, shop.getShopId().toString());

        return OAuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .shopId(shop.getShopId())
                .email(shop.getEmail())
                .role("SHOP")
                .build();
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "USER";
        }
        if (role.startsWith("ROLE_")) {
            return role.substring("ROLE_".length());
        }
        return role.toUpperCase();
    }

    private String generateRandomPassword() {
        return "oauth_" + System.currentTimeMillis();
    }
}
