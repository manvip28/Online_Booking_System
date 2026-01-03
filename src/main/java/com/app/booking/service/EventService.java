package com.app.booking.service;

import com.app.booking.dto.request.EventRequest;
import com.app.booking.dto.response.EventResponse;
import com.app.booking.entity.Event;
import com.app.booking.exception.ResourceNotFoundException;
import com.app.booking.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Cacheable(value = "events")
    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return mapToResponse(event);
    }

    @CacheEvict(value = "events", allEntries = true)
    @Transactional
    public EventResponse createEvent(EventRequest request) {
        Event event = Event.builder()
                .name(request.getName())
                .genre(request.getGenre())
                .language(request.getLanguage())
                .duration(request.getDuration())
                .rating(request.getRating())
                .build();

        Event savedEvent = eventRepository.save(event);
        return mapToResponse(savedEvent);
    }

    @CacheEvict(value = "events", allEntries = true)
    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    @CacheEvict(value = "events", allEntries = true)
    @Transactional
    public EventResponse updateEvent(Long id, EventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        
        event.setName(request.getName());
        event.setGenre(request.getGenre());
        event.setLanguage(request.getLanguage());
        event.setDuration(request.getDuration());
        event.setRating(request.getRating());
        
        Event updatedEvent = eventRepository.save(event);
        return mapToResponse(updatedEvent);
    }

    private EventResponse mapToResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .genre(event.getGenre())
                .language(event.getLanguage())
                .duration(event.getDuration())
                .rating(event.getRating())
                .build();
    }
}
