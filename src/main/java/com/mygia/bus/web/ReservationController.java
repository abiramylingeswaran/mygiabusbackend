package com.mygia.bus.web;

import com.mygia.bus.dto.reservation.BookingBundleResponse;
import com.mygia.bus.dto.reservation.ReservationRequest;
import com.mygia.bus.dto.reservation.ReservationResponse;
import com.mygia.bus.service.AuthService;
import com.mygia.bus.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final AuthService authService;

    public ReservationController(ReservationService reservationService, AuthService authService) {
        this.reservationService = reservationService;
        this.authService = authService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingBundleResponse book(Authentication authentication,
                                      @Valid @RequestBody ReservationRequest request) {
        return reservationService.book(authService.requireUser(authentication.getName()), request);
    }

    @GetMapping("/me")
    public List<ReservationResponse> mine(Authentication authentication) {
        return reservationService.mine(authService.requireUser(authentication.getName()).getId());
    }
}
