package com.app.booking.service;

import com.app.booking.dto.request.ShowRequest;
import com.app.booking.dto.response.ShowResponse;
import com.app.booking.entity.Auditorium;
import com.app.booking.entity.Event;
import com.app.booking.entity.Show;
import com.app.booking.exception.ResourceNotFoundException;
import com.app.booking.repository.AuditoriumRepository;
import com.app.booking.repository.EventRepository;
import com.app.booking.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;
    private final EventRepository eventRepository;
    private final AuditoriumRepository auditoriumRepository;

    @Cacheable(value = "shows", key = "#eventId")
    @Transactional(readOnly = true)
    public List<ShowResponse> getShowsByEvent(Long eventId) {
        return showRepository.findByEventId(eventId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "shows", key = "#request.eventId")
    @Transactional
    public ShowResponse createShow(ShowRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        Auditorium auditorium = auditoriumRepository.findById(request.getAuditoriumId())
                .orElseThrow(() -> new ResourceNotFoundException("Auditorium not found"));

        Show show = Show.builder()
                .event(event)
                .auditorium(auditorium)
                .showTime(request.getShowTime())
                .build();

        Show savedShow = showRepository.save(show);
        return mapToResponse(savedShow);
    }

    private ShowResponse mapToResponse(Show show) {
        return ShowResponse.builder()
                .id(show.getId())
                .eventId(show.getEvent().getId())
                .eventName(show.getEvent().getName())
                .auditoriumId(show.getAuditorium().getId())
                .auditoriumName(show.getAuditorium().getName())
                .showTime(show.getShowTime())
                .build();
    }
}
