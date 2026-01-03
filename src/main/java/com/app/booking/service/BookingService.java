package com.app.booking.service;

import com.app.booking.dto.request.BookingRequest;
import com.app.booking.dto.response.BookingResponse;
import com.app.booking.entity.*;
import com.app.booking.exception.BadRequestException;
import com.app.booking.exception.ConflictException;
import com.app.booking.exception.ResourceNotFoundException;
import com.app.booking.repository.BookingRepository;
import com.app.booking.repository.SeatRepository;
import com.app.booking.repository.ShowRepository;
import com.app.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        // Lock seats to prevent concurrent booking
        List<Seat> seats = seatRepository.findAllByIdInAndLock(request.getSeatIds());
        if (seats.size() != request.getSeatIds().size()) {
            throw new BadRequestException("Some seats not found");
        }

        // Validate seats belong to the correct auditorium
        for (Seat seat : seats) {
            if (!seat.getAuditorium().getId().equals(show.getAuditorium().getId())) {
                throw new BadRequestException("Seat " + seat.getSeatNumber() + " does not belong to the show auditorium");
            }
        }

        // Check for double booking
        List<Booking> conflicts = bookingRepository.findConflictingBookings(show.getId(), request.getSeatIds());
        if (!conflicts.isEmpty()) {
            throw new ConflictException("One or more seats are already booked for this show");
        }

        Booking booking = Booking.builder()
                .user(user)
                .show(show)
                .seats(seats)
                .status(Booking.Status.CONFIRMED)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        try {
            kafkaProducerService.sendBookingConfirmation(savedBooking.getId());
        } catch (Exception e) {
            System.err.println("Failed to send Kafka message: " + e.getMessage());
        }

        return mapToResponse(savedBooking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        // Security check: ensure user owns booking
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!booking.getUser().getEmail().equals(email)) {
             // Access denied logic
        }

        return mapToResponse(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .bookingId(booking.getId())
                .showId(booking.getShow().getId())
                .eventName(booking.getShow().getEvent().getName())
                .seats(booking.getSeats().stream().map(Seat::getSeatNumber).collect(Collectors.toList()))
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
