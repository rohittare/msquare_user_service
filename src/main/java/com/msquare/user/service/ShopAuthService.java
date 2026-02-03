package com.msquare.user.service;

import com.msquare.user.entity.ShopEntity;
import com.msquare.user.model.ShopAuthResponse;
import com.msquare.user.model.ShopLoginRequest;
import com.msquare.user.model.ShopRegisterRequest;
import com.msquare.user.repo.ShopRepo;
import com.msquare.user.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
public class ShopAuthService {
    private final ShopRepo shopRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ShopAuthService(ShopRepo shopRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.shopRepo = shopRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public ShopAuthResponse register(ShopRegisterRequest request) {
        if (shopRepo.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        ShopEntity shop = new ShopEntity();
        shop.setName(request.getName());
        shop.setEmail(request.getEmail());
        shop.setPassword(passwordEncoder.encode(request.getPassword()));
        shop.setPhoneNumber(request.getPhoneNumber());
        shop.setDescription(request.getDescription());
        shop.setTags(request.getTags());
        if (request.getPureVeg() != null) {
            shop.setPureVeg(request.getPureVeg());
        }
        shop.setRating(0.0);
        shop.setApproved(false);
        shop.setRole(normalizeRole(null));

        ShopEntity saved = shopRepo.save(shop);
        return buildResponse(saved);
    }

    public ShopAuthResponse login(ShopLoginRequest request) {
        ShopEntity shop = shopRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (shop.getPassword() == null || !passwordEncoder.matches(request.getPassword(), shop.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return buildResponse(shop);
    }

    private ShopAuthResponse buildResponse(ShopEntity shop) {
        if (shop.getShopId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Shop ID is missing");
        }

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", normalizeRole(shop.getRole()));

        String token = jwtService.generateToken(extraClaims, shop.getShopId().toString());
        return ShopAuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .shopId(shop.getShopId())
                .email(shop.getEmail())
                .role(normalizeRole(shop.getRole()))
                .build();
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "SHOP";
        }
        if (role.startsWith("ROLE_")) {
            return role.substring("ROLE_".length());
        }
        return role;
    }
}
