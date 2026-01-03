package com.app.booking.repository;

import com.app.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    
    @Query("SELECT b FROM Booking b JOIN b.seats s WHERE b.show.id = :showId AND s.id IN :seatIds AND b.status <> 'CANCELLED'")
    List<Booking> findConflictingBookings(@Param("showId") Long showId, @Param("seatIds") List<Long> seatIds);
}
