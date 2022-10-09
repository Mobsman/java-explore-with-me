package ru.practicum.main.event.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.entity.EventState;
import ru.practicum.main.event.request.AdminUpdateEventRequest;
import ru.practicum.main.event.service.AdminEventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final AdminEventService service;

    @GetMapping
    public List<EventFullDto> getEvents(
            @RequestParam List<Long> users,
            @RequestParam List<EventState> states,
            @RequestParam List<Long> categories,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size
    ) {

        return service.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);

    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable("eventId") @Positive Long eventId,
                                           @RequestBody @Valid AdminUpdateEventRequest request) {
        return service.updateEventByAdmin(eventId, request);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publicationEvent(@PathVariable("eventId") @Positive Long eventId) {
        return service.publicationEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable("eventId") @Positive Long eventId) {
        return service.rejectEvent(eventId);
    }

}
