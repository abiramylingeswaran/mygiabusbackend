package com.mygia.bus.service;

import com.mygia.bus.domain.Bus;
import com.mygia.bus.dto.bus.BusRequest;
import com.mygia.bus.dto.bus.BusResponse;
import com.mygia.bus.exception.ApiException;
import com.mygia.bus.repository.BusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BusService {

    private final BusRepository busRepository;

    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public List<BusResponse> findAll() {
        return busRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional
    public BusResponse create(BusRequest request) {
        validateSeats(request.totalSeats());
        if (busRepository.existsByBusNumberIgnoreCase(request.busNumber())) {
            throw new ApiException("Bus number already exists");
        }
        Bus saved = busRepository.save(Bus.builder()
                .busNumber(request.busNumber().trim().toUpperCase())
                .totalSeats(request.totalSeats())
                .build());
        return toDto(saved);
    }

    @Transactional
    public BusResponse update(Long id, BusRequest request) {
        validateSeats(request.totalSeats());
        Bus bus = busRepository.findById(id).orElseThrow(() -> new ApiException("Bus not found"));
        bus.setBusNumber(request.busNumber().trim().toUpperCase());
        bus.setTotalSeats(request.totalSeats());
        return toDto(bus);
    }

    @Transactional
    public void delete(Long id) {
        if (!busRepository.existsById(id)) {
            throw new ApiException("Bus not found");
        }
        busRepository.deleteById(id);
    }

    private void validateSeats(int total) {
        if (total % 4 != 0) {
            throw new ApiException("Total seats must be a multiple of 4 for the 2x2 layout");
        }
    }

    private BusResponse toDto(Bus bus) {
        return new BusResponse(bus.getId(), bus.getBusNumber(), bus.getTotalSeats());
    }
}
