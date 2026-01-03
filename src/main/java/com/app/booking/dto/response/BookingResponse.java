package com.app.booking.dto.response;

import com.app.booking.entity.Booking.Status;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookingResponse {
    private Long bookingId;
    private Long showId;
    private String eventName;
    private List<String> seats;
    private Status status;
    private LocalDateTime createdAt;
}
