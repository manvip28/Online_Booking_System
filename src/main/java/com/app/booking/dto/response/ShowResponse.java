package com.app.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long eventId;
    private String eventName;
    private Long auditoriumId;
    private String auditoriumName;
    private LocalDateTime showTime;
}
