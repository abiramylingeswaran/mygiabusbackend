package com.mygia.bus.service;

import com.mygia.bus.domain.Reservation;
import com.mygia.bus.domain.Route;
import com.mygia.bus.domain.User;
import com.mygia.bus.domain.enums.ReservationStatus;
import com.mygia.bus.dto.reservation.BookingBundleResponse;
import com.mygia.bus.dto.reservation.ReservationRequest;
import com.mygia.bus.dto.reservation.ReservationResponse;
import com.mygia.bus.exception.ApiException;
import com.mygia.bus.exception.SeatUnavailableException;
import com.mygia.bus.repository.ReservationRepository;
import com.mygia.bus.repository.RouteRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RouteRepository routeRepository;
    private final NotificationService notificationService;

    public ReservationService(ReservationRepository reservationRepository,
                              RouteRepository routeRepository,
                              NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.routeRepository = routeRepository;
        this.notificationService = notificationService;
    }

    /**
     * Pessimistic lock on the route row + unique partial index on (route, seat)
     * where status = CONFIRMED prevents double-booking under concurrent load.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingBundleResponse book(User user, ReservationRequest request) {
        Set<String> seats = new LinkedHashSet<>(request.seatNumbers());
        Route route = routeRepository.findByIdForUpdate(request.routeId())
                .orElseThrow(() -> new ApiException("Route not found"));

        List<String> valid = SeatLayout.generate(route.getBus().getTotalSeats());
        for (String seat : seats) {
            if (!valid.contains(seat)) {
                throw failAndNotify(user, "Invalid seat " + seat);
            }
            if (reservationRepository.existsByRouteIdAndSeatNumberAndStatus(
                    route.getId(), seat, ReservationStatus.CONFIRMED)) {
                throw failAndNotify(user, "Seat " + seat + " is already booked");
            }
        }

        try {
            route.getOrigin().getName();
            route.getDestination().getName();
            route.getBus().getBusNumber();
            List<Reservation> saved = new ArrayList<>();
            for (String seat : seats) {
                Reservation reservation = Reservation.builder()
                        .user(user)
                        .route(route)
                        .seatNumber(seat)
                        .status(ReservationStatus.CONFIRMED)
                        .build();
                saved.add(reservationRepository.saveAndFlush(reservation));
            }
            notificationService.notifyReservationSuccess(user, saved);
            return new BookingBundleResponse(saved.stream().map(this::toDto).toList(), "Reservation confirmed");
        } catch (DataIntegrityViolationException ex) {
            throw failAndNotify(user, "One or more seats were taken by another customer");
        }
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> mine(Long userId) {
        return reservationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(r -> {
                    r.getRoute().getOrigin().getName();
                    r.getRoute().getDestination().getName();
                    r.getRoute().getBus().getBusNumber();
                    return toDto(r);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> all() {
        return reservationRepository.findAllWithDetails().stream().map(this::toDto).toList();
    }

    @Transactional
    public ReservationResponse cancel(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ApiException("Reservation not found"));
        reservation.setStatus(ReservationStatus.CANCELLED);
        return toDto(reservation);
    }

    private SeatUnavailableException failAndNotify(User user, String reason) {
        notificationService.notifyReservationFailure(user, reason);
        return new SeatUnavailableException(reason);
    }

    private ReservationResponse toDto(Reservation r) {
        return new ReservationResponse(
                r.getId(),
                r.getRoute().getId(),
                r.getRoute().getOrigin().getName(),
                r.getRoute().getDestination().getName(),
                r.getRoute().getBus().getBusNumber(),
                r.getRoute().getDepartureTime(),
                r.getRoute().getArrivalTime(),
                r.getSeatNumber(),
                r.getStatus().name(),
                r.getRoute().getPrice(),
                r.getCreatedAt()
        );
    }
}
