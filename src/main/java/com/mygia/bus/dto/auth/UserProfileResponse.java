package com.mygia.bus.dto.auth;

public record UserProfileResponse(
        Long id,
        String fullName,
        String address,
        String phoneNumber,
        String email,
        String role
) {
}
