package com.mygia.bus.dto.route;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RouteResponse(
        Long id,
        Long busId,
        String busNumber,
        Integer totalSeats,
        Long originId,
        String originName,
        Long destinationId,
        String destinationName,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        BigDecimal price,
        int availableSeats
) {
}
