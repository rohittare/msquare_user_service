package com.msquare.user.api;

import com.msquare.user.model.ShopAuthResponse;
import com.msquare.user.model.ShopLoginRequest;
import com.msquare.user.model.ShopRegisterRequest;
import com.msquare.user.service.ShopAuthService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/auth/shop")
public class ShopAuthAPI {
    private final ShopAuthService shopAuthService;

    public ShopAuthAPI(ShopAuthService shopAuthService) {
        this.shopAuthService = shopAuthService;
    }

    @PostMapping("/register")
    public ShopAuthResponse register(@RequestBody ShopRegisterRequest request) {
        return shopAuthService.register(request);
    }

    @PostMapping("/login")
    public ShopAuthResponse login(@RequestBody ShopLoginRequest request) {
        return shopAuthService.login(request);
    }
}
