package com.mygia.bus.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(max = 150) String fullName,
        @NotBlank @Size(max = 500) String address,
        @NotBlank @Pattern(regexp = "^\\+[1-9]\\d{6,14}$", message = "Phone must be E.164, e.g. +94XXXXXXXXX")
        String phoneNumber,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 72) String password
) {
}
