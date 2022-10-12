package ru.practicum.main.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.event.dto.EventDtoConverter;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.event.entity.EventState;
import ru.practicum.main.event.exception.EventNotFoundException;
import ru.practicum.main.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class PublicEventService {

    private final EventRepository eventRepository;
    private final EventDtoConverter eventDtoConverter;


    public List<Event> searchEvents(String text, List<Long> categories, boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, int from, int size) {

        return eventRepository.findEvents(text, categories, paid, rangeStart, rangeEnd,
                EventState.PUBLISHED, PageRequest.of(from / size, size));

    }

    public EventFullDto getEventById(Long eventId) {
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new EventNotFoundException("Событие не найдено");
        }

        return eventDtoConverter.convertToEventFullDto(eventRepository.findByIdAndState(eventId, EventState.PUBLISHED));
    }


}
