package com.mygia.bus.dto.route;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RouteRequest(
        @NotNull Long busId,
        @NotNull Long originId,
        @NotNull Long destinationId,
        @NotNull LocalDateTime departureTime,
        @NotNull LocalDateTime arrivalTime,
        @NotNull @DecimalMin("0.0") BigDecimal price
) {
}
