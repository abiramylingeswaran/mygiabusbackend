package com.mygia.bus.dto.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocationRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 120) String district
) {
}
