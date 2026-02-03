package com.msquare.user.security;

import com.msquare.user.entity.ShopEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class ShopPrincipal implements UserDetails {
    private final ShopEntity shop;

    public ShopPrincipal(ShopEntity shop) {
        this.shop = shop;
    }

    public UUID getShopId() {
        return shop.getShopId();
    }

    public String getRole() {
        return shop.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(normalizeRole(shop.getRole())));
    }

    @Override
    public String getPassword() {
        return shop.getPassword();
    }

    @Override
    public String getUsername() {
        return shop.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "ROLE_SHOP";
        }
        if (role.startsWith("ROLE_")) {
            return role;
        }
        return "ROLE_" + role;
    }
}
