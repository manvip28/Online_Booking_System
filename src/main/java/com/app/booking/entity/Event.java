package com.app.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String genre;

    @Column(nullable = false)
    private String language;

    private int duration; // in minutes

    private double rating;
}
