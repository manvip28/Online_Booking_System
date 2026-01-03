package com.app.booking.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShowRequest {
    @NotNull
    private Long eventId;
    @NotNull
    private Long auditoriumId;
    @NotNull
    private LocalDateTime showTime;
}
