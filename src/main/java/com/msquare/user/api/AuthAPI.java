package com.msquare.user.api;

import com.msquare.user.model.AuthResponse;
import com.msquare.user.model.LoginRequest;
import com.msquare.user.model.OAuthRequest;
import com.msquare.user.model.OAuthResponse;
import com.msquare.user.model.RegisterRequest;
import com.msquare.user.service.AuthService;
import com.msquare.user.service.OAuthService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthAPI {
    private final AuthService authService;
    private final OAuthService oauthService;

    public AuthAPI(AuthService authService, OAuthService oauthService) {
        this.authService = authService;
        this.oauthService = oauthService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/oauth")
    public OAuthResponse oauth(@RequestBody OAuthRequest request) {
        return oauthService.authenticate(request);
    }
}
