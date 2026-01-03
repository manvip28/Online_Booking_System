package com.app.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "auditoriums")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auditorium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    @ToString.Exclude
    private Venue venue;

    @OneToMany(mappedBy = "auditorium", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Seat> seats;
}
