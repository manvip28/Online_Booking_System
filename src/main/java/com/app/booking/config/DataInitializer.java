package com.app.booking.config;

import com.app.booking.entity.*;
import com.app.booking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VenueRepository venueRepository;
    private final AuditoriumRepository auditoriumRepository;
    private final SeatRepository seatRepository;
    private final EventRepository eventRepository;
    private final ShowRepository showRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@test.com")) {
            User admin = User.builder()
                    .name("Admin User")
                    .email("admin@test.com")
                    .phone("0000000000")
                    .passwordHash(passwordEncoder.encode("password"))
                    .role(User.Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println("Seeded Admin User: admin@test.com / password");
        }

        if (!userRepository.existsByEmail("user@test.com")) {
            User user = User.builder()
                    .name("Normal User")
                    .email("user@test.com")
                    .phone("1111111111")
                    .passwordHash(passwordEncoder.encode("password"))
                    .role(User.Role.USER)
                    .build();
            userRepository.save(user);
            System.out.println("Seeded Normal User: user@test.com / password");
        }

        if (venueRepository.count() == 0) {
            Venue venue = Venue.builder()
                    .name("PVR Cinemas")
                    .city("Mumbai")
                    .location("Phoenix Mall")
                    .build();
            venueRepository.save(venue);

            Auditorium audi = Auditorium.builder()
                    .name("Screen 1")
                    .venue(venue)
                    .build();
            auditoriumRepository.save(audi);

            List<Seat> seats = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                seats.add(Seat.builder()
                        .seatNumber("A" + i)
                        .category(Seat.Category.GOLD)
                        .auditorium(audi)
                        .build());
            }
            seatRepository.saveAll(seats);
            System.out.println("Seeded Venue, Auditorium, and 10 Seats");

            Event event = Event.builder()
                    .name("Avengers: Secret Wars")
                    .genre("Action/Sci-Fi")
                    .language("English")
                    .duration(180)
                    .rating(4.9)
                    .build();
            eventRepository.save(event);

            Show show = Show.builder()
                    .event(event)
                    .auditorium(audi)
                    .showTime(LocalDateTime.now().plusDays(1).withHour(18).withMinute(0))
                    .build();
            showRepository.save(show);
            System.out.println("Seeded Event and Show for tomorrow 6 PM");
        }
    }
}
