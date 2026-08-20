package com.mygia.bus.dto.auth;

public record AuthResponse(
        String token,
        String email,
        String fullName,
        String role,
        Long userId
) {
}
