package com.fierceadventurer.analyticsservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.UUID;

@Entity
@Table(name = "optimal_time_slots")
@Getter
@Setter
@NoArgsConstructor
public class OptimalTimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false,  unique = false)
    private UUID socialAccountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private int hourOfDay;

    @Column(nullable = false)
    private double engagementScore;
}
