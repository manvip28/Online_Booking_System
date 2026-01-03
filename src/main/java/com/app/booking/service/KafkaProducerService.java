package com.app.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "booking_notifications";

    public void sendBookingConfirmation(Long bookingId) {
        log.info("Sending booking confirmation for booking ID: {}", bookingId);
        kafkaTemplate.send(TOPIC, "Booking Confirmed: " + bookingId);
    }
}
