package com.mygia.bus.web;

import com.mygia.bus.dto.location.LocationResponse;
import com.mygia.bus.dto.route.RouteResponse;
import com.mygia.bus.dto.seat.SeatStatusResponse;
import com.mygia.bus.service.LocationService;
import com.mygia.bus.service.RouteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PublicCatalogController {

    private final LocationService locationService;
    private final RouteService routeService;

    public PublicCatalogController(LocationService locationService, RouteService routeService) {
        this.locationService = locationService;
        this.routeService = routeService;
    }

    @GetMapping("/locations/search")
    public List<LocationResponse> searchLocations(@RequestParam("q") String query) {
        return locationService.search(query);
    }

    @GetMapping("/locations")
    public List<LocationResponse> allLocations() {
        return locationService.findAll();
    }

    @GetMapping("/routes")
    public List<RouteResponse> searchRoutes(
            @RequestParam(required = false) Long originId,
            @RequestParam(required = false) Long destinationId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return routeService.search(originId, destinationId, date);
    }

    @GetMapping("/routes/{id}")
    public RouteResponse getRoute(@PathVariable Long id) {
        return routeService.get(id);
    }

    @GetMapping("/routes/{id}/seats")
    public List<SeatStatusResponse> seats(@PathVariable Long id) {
        return routeService.seats(id);
    }
}
