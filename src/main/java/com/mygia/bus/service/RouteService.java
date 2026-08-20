package com.mygia.bus.service;

import com.mygia.bus.domain.Bus;
import com.mygia.bus.domain.Location;
import com.mygia.bus.domain.Route;
import com.mygia.bus.domain.enums.ReservationStatus;
import com.mygia.bus.dto.route.RouteRequest;
import com.mygia.bus.dto.route.RouteResponse;
import com.mygia.bus.dto.seat.SeatStatusResponse;
import com.mygia.bus.exception.ApiException;
import com.mygia.bus.repository.BusRepository;
import com.mygia.bus.repository.LocationRepository;
import com.mygia.bus.repository.ReservationRepository;
import com.mygia.bus.repository.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final BusRepository busRepository;
    private final LocationRepository locationRepository;
    private final ReservationRepository reservationRepository;

    public RouteService(RouteRepository routeRepository,
                        BusRepository busRepository,
                        LocationRepository locationRepository,
                        ReservationRepository reservationRepository) {
        this.routeRepository = routeRepository;
        this.busRepository = busRepository;
        this.locationRepository = locationRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional(readOnly = true)
    public List<RouteResponse> findAllAdmin() {
        return routeRepository.findAllWithDetails().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<RouteResponse> search(Long originId, Long destinationId, LocalDate date) {
        LocalDateTime from;
        LocalDateTime to;
        if (date != null) {
            from = date.atStartOfDay();
            to = date.plusDays(1).atStartOfDay();
        } else {
            from = LocalDateTime.now();
            to = from.plusYears(1);
        }
        if (originId != null && destinationId != null) {
            return routeRepository.search(originId, destinationId, from, to).stream().map(this::toDto).toList();
        }
        return routeRepository.findUpcoming(from).stream()
                .filter(r -> originId == null || r.getOrigin().getId().equals(originId))
                .filter(r -> destinationId == null || r.getDestination().getId().equals(destinationId))
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public RouteResponse get(Long id) {
        Route route = routeRepository.findById(id).orElseThrow(() -> new ApiException("Route not found"));
        route.getBus().getBusNumber();
        route.getOrigin().getName();
        route.getDestination().getName();
        return toDto(route);
    }

    @Transactional(readOnly = true)
    public List<SeatStatusResponse> seats(Long routeId) {
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new ApiException("Route not found"));
        Set<String> booked = reservationRepository.findBookedSeats(routeId, ReservationStatus.CONFIRMED);
        List<SeatStatusResponse> result = new ArrayList<>();
        for (String seat : SeatLayout.generate(route.getBus().getTotalSeats())) {
            result.add(new SeatStatusResponse(seat, booked.contains(seat) ? "BOOKED" : "AVAILABLE"));
        }
        return result;
    }

    @Transactional
    public RouteResponse create(RouteRequest request) {
        Route route = new Route();
        apply(route, request);
        return toDto(routeRepository.save(route));
    }

    @Transactional
    public RouteResponse update(Long id, RouteRequest request) {
        Route route = routeRepository.findById(id).orElseThrow(() -> new ApiException("Route not found"));
        apply(route, request);
        return toDto(route);
    }

    @Transactional
    public void delete(Long id) {
        if (!routeRepository.existsById(id)) {
            throw new ApiException("Route not found");
        }
        routeRepository.deleteById(id);
    }

    private void apply(Route route, RouteRequest request) {
        if (request.originId().equals(request.destinationId())) {
            throw new ApiException("Origin and destination must differ");
        }
        if (!request.arrivalTime().isAfter(request.departureTime())) {
            throw new ApiException("Arrival must be after departure");
        }
        Bus bus = busRepository.findById(request.busId()).orElseThrow(() -> new ApiException("Bus not found"));
        Location origin = locationRepository.findById(request.originId()).orElseThrow(() -> new ApiException("Origin not found"));
        Location dest = locationRepository.findById(request.destinationId()).orElseThrow(() -> new ApiException("Destination not found"));
        route.setBus(bus);
        route.setOrigin(origin);
        route.setDestination(dest);
        route.setDepartureTime(request.departureTime());
        route.setArrivalTime(request.arrivalTime());
        route.setPrice(request.price());
    }

    private RouteResponse toDto(Route r) {
        int booked = reservationRepository.findBookedSeats(r.getId(), ReservationStatus.CONFIRMED).size();
        int available = Math.max(0, r.getBus().getTotalSeats() - booked);
        return new RouteResponse(
                r.getId(),
                r.getBus().getId(),
                r.getBus().getBusNumber(),
                r.getBus().getTotalSeats(),
                r.getOrigin().getId(),
                r.getOrigin().getName(),
                r.getDestination().getId(),
                r.getDestination().getName(),
                r.getDepartureTime(),
                r.getArrivalTime(),
                r.getPrice(),
                available
        );
    }
}
