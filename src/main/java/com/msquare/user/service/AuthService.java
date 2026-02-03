package com.msquare.user.service;

import com.msquare.user.entity.UserDetailEntity;
import com.msquare.user.model.AuthResponse;
import com.msquare.user.model.LoginRequest;
import com.msquare.user.model.RegisterRequest;
import com.msquare.user.repo.UserDetailRepo;
import com.msquare.user.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserDetailRepo userDetailRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserDetailRepo userDetailRepo,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userDetailRepo = userDetailRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userDetailRepo.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        UserDetailEntity user = new UserDetailEntity();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(normalizeRole(request.getRole()));
        user.setCreatedDate(LocalDateTime.now());
        user.setActive(true);

        UserDetailEntity saved = userDetailRepo.save(user);
        return buildResponse(saved);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetailEntity user = userDetailRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        return buildResponse(user);
    }

    private AuthResponse buildResponse(UserDetailEntity user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", normalizeRole(user.getRole()));

        if (user.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User ID is missing");
        }

        String token = jwtService.generateToken(extraClaims, user.getUserId().toString());
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .email(user.getEmail())
                .role(normalizeRole(user.getRole()))
                .build();
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "USER";
        }
        if (role.startsWith("ROLE_")) {
            return role.substring("ROLE_".length());
        }
        return role;
    }

}
