package com.app.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "booking_notifications", groupId = "booking-group")
    public void listen(String message) {
        log.info("RECEIVED: {} - Sending Email/SMS notification...", message);
    }
}
