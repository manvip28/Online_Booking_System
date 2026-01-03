package com.app.booking.service;

import com.app.booking.dto.request.BookingRequest;
import com.app.booking.entity.*;
import com.app.booking.repository.BookingRepository;
import com.app.booking.repository.SeatRepository;
import com.app.booking.repository.ShowRepository;
import com.app.booking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ShowRepository showRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void testCreateBooking_Success() {
        // Mock Security
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@test.com");
        SecurityContextHolder.setContext(securityContext);

        // Mock Data
        User user = new User();
        user.setEmail("test@test.com");
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        Auditorium auditorium = new Auditorium();
        auditorium.setId(1L);

        Show show = new Show();
        show.setId(1L);
        show.setAuditorium(auditorium);
        Event event = new Event();
        event.setName("Test Event");
        show.setEvent(event);
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("A1");
        seat.setAuditorium(auditorium);
        when(seatRepository.findAllByIdInAndLock(any())).thenReturn(Arrays.asList(seat));

        when(bookingRepository.findConflictingBookings(any(), any())).thenReturn(Collections.emptyList());
        
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setShow(show);
        booking.setSeats(Arrays.asList(seat));
        booking.setStatus(Booking.Status.CONFIRMED);
        when(bookingRepository.save(any())).thenReturn(booking);

        // Execute
        BookingRequest request = new BookingRequest();
        request.setShowId(1L);
        request.setSeatIds(Arrays.asList(1L));

        assertNotNull(bookingService.createBooking(request));
    }
}
