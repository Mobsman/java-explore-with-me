package ru.practicum.main.event.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.event.dto.EventDtoConverter;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.event.entity.EventState;
import ru.practicum.main.event.exception.EventNotFoundException;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.event.request.AdminUpdateEventRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final EventRepository repository;
    private final EventDtoConverter converter;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size
    ) {

        Sort sort = Sort.by(Sort.Direction.ASC, "eventDate");

        return repository.findWithAdminParameters(
                        users, states, categories, rangeStart, rangeEnd, PageRequest.of(from / size, size, sort)).stream().
                map(converter::convertToEventFullDto).collect(Collectors.toList());

    }

    public EventFullDto updateEventByAdmin(Long eventId, AdminUpdateEventRequest request) {

        if (eventRepository.findById(eventId).isEmpty()) {
            throw new EventNotFoundException("Событие не найдено");
        }

        Event event = eventRepository.findById(eventId).get();

        event.setAnnotation(request.getAnnotation());
        if (request.getCategoryId() != null) {
            event.setCategory(categoryRepository.findById(request.getCategoryId()).get());
        }
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setPaid(request.getPaid());
        event.setParticipantLimit(request.getParticipantLimit());
        event.setTitle(request.getTitle());
        event.setRequestModeration(request.isRequestModeration());
        if (request.getLocation() != null) {
            event.setLat(request.getLocation().getLat());
        }
        if (request.getLocation() != null) {
            event.setLon(request.getLocation().getLon());
        }


        return converter.convertToEventFullDto(eventRepository.save(event));
    }

    public EventFullDto publicationEvent(Long eventId) {

        if (eventRepository.findById(eventId).isEmpty()) {
            throw new EventNotFoundException("Событие не найдено");
        }

        Event event = eventRepository.findById(eventId).get();

        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());

        return converter.convertToEventFullDto(eventRepository.save(event));
    }

    public EventFullDto rejectEvent(Long eventId) {
        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Событие не найдено");
        }

        Event event = eventRepository.findById(eventId).get();

        event.setState(EventState.CANCELED);
        event.setPublishedOn(null);

        return converter.convertToEventFullDto(eventRepository.save(event));
    }
}
