package com.msquare.user.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.msquare.user.repo.ShopRepo;
import com.msquare.user.security.ShopPrincipal;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ShopRepo shopRepo;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            ShopRepo shopRepo
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.shopRepo = shopRepo;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String subject = jwtService.extractSubject(jwt);
            final String role = jwtService.extractClaim(jwt, claims -> claims.get("role", String.class));

            if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (isShopRole(role)) {
                    authenticateShop(request, jwt, subject);
                } else {
                    authenticateUser(request, jwt, subject);
                }
            }
        } catch (Exception ignored) {
            // Invalid token should not block public endpoints.
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(HttpServletRequest request, String jwt, String subject) {
        try {
            var userDetails = userDetailsService.loadUserByUsername(subject);
            if (jwtService.isTokenValid(jwt, subject)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception ignored) {
        }
    }

    private void authenticateShop(HttpServletRequest request, String jwt, String subject) {
        try {
            UUID shopId = UUID.fromString(subject);
            var shop = shopRepo.findById(shopId).orElse(null);
            if (shop == null) {
                return;
            }
            if (jwtService.isTokenValid(jwt, subject)) {
                var principal = new ShopPrincipal(shop);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    private boolean isShopRole(String role) {
        if (role == null) {
            return false;
        }
        return "SHOP".equalsIgnoreCase(role) || "ROLE_SHOP".equalsIgnoreCase(role);
    }
}
