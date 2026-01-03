package com.app.booking.controller;

import com.app.booking.dto.request.ShowRequest;
import com.app.booking.dto.response.ShowResponse;
import com.app.booking.service.ShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ShowController {

    private final ShowService showService;

    @GetMapping("/events/{eventId}/shows")
    public ResponseEntity<List<ShowResponse>> getShowsByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(showService.getShowsByEvent(eventId));
    }

    @PostMapping("/admin/shows")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowResponse> createShow(@Valid @RequestBody ShowRequest request) {
        return ResponseEntity.ok(showService.createShow(request));
    }
}
