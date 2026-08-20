package com.mygia.bus.repository;

import com.mygia.bus.domain.Reservation;
import com.mygia.bus.domain.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("""
            SELECT r FROM Reservation r
            JOIN FETCH r.user
            JOIN FETCH r.route rt
            JOIN FETCH rt.origin
            JOIN FETCH rt.destination
            JOIN FETCH rt.bus
            ORDER BY r.createdAt DESC
            """)
    List<Reservation> findAllWithDetails();

    @Query("""
            SELECT r.seatNumber FROM Reservation r
            WHERE r.route.id = :routeId AND r.status = :status
            """)
    Set<String> findBookedSeats(@Param("routeId") Long routeId,
                                @Param("status") ReservationStatus status);

    boolean existsByRouteIdAndSeatNumberAndStatus(Long routeId, String seatNumber, ReservationStatus status);
}
