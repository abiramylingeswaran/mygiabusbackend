package com.mygia.bus.dto.reservation;

import java.util.List;

public record BookingBundleResponse(
        List<ReservationResponse> tickets,
        String message
) {
}
