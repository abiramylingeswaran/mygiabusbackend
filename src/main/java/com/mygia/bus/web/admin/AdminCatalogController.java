package com.mygia.bus.web.admin;

import com.mygia.bus.dto.bus.BusRequest;
import com.mygia.bus.dto.bus.BusResponse;
import com.mygia.bus.dto.location.LocationRequest;
import com.mygia.bus.dto.location.LocationResponse;
import com.mygia.bus.dto.reservation.ReservationResponse;
import com.mygia.bus.dto.route.RouteRequest;
import com.mygia.bus.dto.route.RouteResponse;
import com.mygia.bus.service.BusService;
import com.mygia.bus.service.LocationService;
import com.mygia.bus.service.ReservationService;
import com.mygia.bus.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminCatalogController {

    private final LocationService locationService;
    private final BusService busService;
    private final RouteService routeService;
    private final ReservationService reservationService;

    public AdminCatalogController(LocationService locationService,
                                  BusService busService,
                                  RouteService routeService,
                                  ReservationService reservationService) {
        this.locationService = locationService;
        this.busService = busService;
        this.routeService = routeService;
        this.reservationService = reservationService;
    }

    @GetMapping("/locations")
    public List<LocationResponse> locations() {
        return locationService.findAll();
    }

    @PostMapping("/locations")
    @ResponseStatus(HttpStatus.CREATED)
    public LocationResponse createLocation(@Valid @RequestBody LocationRequest request) {
        return locationService.create(request);
    }

    @PutMapping("/locations/{id}")
    public LocationResponse updateLocation(@PathVariable Long id, @Valid @RequestBody LocationRequest request) {
        return locationService.update(id, request);
    }

    @DeleteMapping("/locations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocation(@PathVariable Long id) {
        locationService.delete(id);
    }

    @GetMapping("/buses")
    public List<BusResponse> buses() {
        return busService.findAll();
    }

    @PostMapping("/buses")
    @ResponseStatus(HttpStatus.CREATED)
    public BusResponse createBus(@Valid @RequestBody BusRequest request) {
        return busService.create(request);
    }

    @PutMapping("/buses/{id}")
    public BusResponse updateBus(@PathVariable Long id, @Valid @RequestBody BusRequest request) {
        return busService.update(id, request);
    }

    @DeleteMapping("/buses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBus(@PathVariable Long id) {
        busService.delete(id);
    }

    @GetMapping("/routes")
    public List<RouteResponse> routes() {
        return routeService.findAllAdmin();
    }

    @PostMapping("/routes")
    @ResponseStatus(HttpStatus.CREATED)
    public RouteResponse createRoute(@Valid @RequestBody RouteRequest request) {
        return routeService.create(request);
    }

    @PutMapping("/routes/{id}")
    public RouteResponse updateRoute(@PathVariable Long id, @Valid @RequestBody RouteRequest request) {
        return routeService.update(id, request);
    }

    @DeleteMapping("/routes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoute(@PathVariable Long id) {
        routeService.delete(id);
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> reservations() {
        return reservationService.all();
    }

    @PatchMapping("/reservations/{id}/cancel")
    public ReservationResponse cancel(@PathVariable Long id) {
        return reservationService.cancel(id);
    }
}
