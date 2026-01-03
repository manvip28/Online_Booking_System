package com.app.booking.repository;

import com.app.booking.entity.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditoriumRepository extends JpaRepository<Auditorium, Long> {
    List<Auditorium> findByVenueId(Long venueId);
}
