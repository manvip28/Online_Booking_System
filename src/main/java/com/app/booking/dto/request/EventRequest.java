package com.app.booking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EventRequest {
    @NotBlank
    private String name;
    private String genre;
    @NotBlank
    private String language;
    @Min(1)
    private int duration;
    private double rating;
}
