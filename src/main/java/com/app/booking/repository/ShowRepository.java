package com.app.booking.repository;

import com.app.booking.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByEventId(Long eventId);
}
