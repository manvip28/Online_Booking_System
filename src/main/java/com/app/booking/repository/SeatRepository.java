package com.app.booking.repository;

import com.app.booking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("SELECT s FROM Seat s WHERE s.id IN :ids")
    List<Seat> findAllByIdInAndLock(@org.springframework.data.repository.query.Param("ids") List<Long> ids);
    
    List<Seat> findByAuditoriumId(Long auditoriumId);
}
