package com.mygia.bus.dto.reservation;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long routeId,
        String origin,
        String destination,
        String busNumber,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        String seatNumber,
        String status,
        BigDecimal price,
        Instant createdAt
) {
}
