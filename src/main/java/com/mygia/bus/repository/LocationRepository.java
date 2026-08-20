package com.mygia.bus.repository;

import com.mygia.bus.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("""
            SELECT l FROM Location l
            WHERE LOWER(l.name) LIKE LOWER(CONCAT(:query, '%'))
            ORDER BY l.name ASC
            """)
    List<Location> searchByNamePrefix(@Param("query") String query);

    boolean existsByNameIgnoreCase(String name);
}
