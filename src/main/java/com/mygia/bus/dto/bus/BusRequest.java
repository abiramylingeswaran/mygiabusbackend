package com.mygia.bus.dto.bus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BusRequest(
        @NotBlank String busNumber,
        @NotNull @Min(4) Integer totalSeats
) {
}
