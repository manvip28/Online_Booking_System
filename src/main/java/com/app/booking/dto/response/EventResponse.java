package com.app.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String genre;
    private String language;
    private int duration;
    private double rating;
}
