package com.mygia.bus.repository;

import com.mygia.bus.domain.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus, Long> {
    Optional<Bus> findByBusNumberIgnoreCase(String busNumber);
    boolean existsByBusNumberIgnoreCase(String busNumber);
}
