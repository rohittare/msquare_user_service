package com.msquare.user.security;

import com.msquare.user.repo.UserDetailRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserDetailRepo userDetailRepo;

    public CustomUserDetailsService(UserDetailRepo userDetailRepo) {
        this.userDetailRepo = userDetailRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String value) throws UsernameNotFoundException {
        var user = findByIdOrEmail(value);
        return new UserPrincipal(user);
    }

    private com.msquare.user.entity.UserDetailEntity findByIdOrEmail(String value) {
        if (value == null || value.isBlank()) {
            throw new UsernameNotFoundException("User not found");
        }
        try {
            UUID id = UUID.fromString(value);
            return userDetailRepo.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } catch (IllegalArgumentException ignored) {
        }
        return userDetailRepo.findByEmail(value)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
