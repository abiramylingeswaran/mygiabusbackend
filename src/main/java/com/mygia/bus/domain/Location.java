package com.mygia.bus.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "locations", indexes = {
        @Index(name = "idx_locations_name_lower", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 120)
    private String district;
}
