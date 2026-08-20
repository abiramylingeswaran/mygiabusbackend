package com.mygia.bus.repository;

import com.mygia.bus.domain.Route;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("""
            SELECT r FROM Route r
            JOIN FETCH r.bus
            JOIN FETCH r.origin
            JOIN FETCH r.destination
            WHERE r.origin.id = :originId
              AND r.destination.id = :destinationId
              AND r.departureTime >= :from
              AND r.departureTime < :to
            ORDER BY r.departureTime ASC
            """)
    List<Route> search(@Param("originId") Long originId,
                       @Param("destinationId") Long destinationId,
                       @Param("from") LocalDateTime from,
                       @Param("to") LocalDateTime to);

    @Query("""
            SELECT r FROM Route r
            JOIN FETCH r.bus
            JOIN FETCH r.origin
            JOIN FETCH r.destination
            WHERE r.departureTime >= :from
            ORDER BY r.departureTime ASC
            """)
    List<Route> findUpcoming(@Param("from") LocalDateTime from);

    @Query("""
            SELECT r FROM Route r
            JOIN FETCH r.bus
            JOIN FETCH r.origin
            JOIN FETCH r.destination
            ORDER BY r.departureTime DESC
            """)
    List<Route> findAllWithDetails();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Route r WHERE r.id = :id")
    Optional<Route> findByIdForUpdate(@Param("id") Long id);
}
