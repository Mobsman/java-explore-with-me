package ru.practicum.main.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.request.NewEventRequest;
import ru.practicum.main.event.request.UpdateEventRequest;
import ru.practicum.main.event.service.PrivateEventService;
import ru.practicum.main.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final PrivateEventService eventService;


    @GetMapping
    public List<EventShortDto> getEventsByUser(@PathVariable("userId") @Positive Long userId,
                                               @RequestParam(value = "from", defaultValue = "0")
                                               @PositiveOrZero Integer from,
                                               @RequestParam(value = "size", defaultValue = "10")
                                               @Positive Integer size) {

        return eventService.getEventsByUser(userId, from, size).getContent();
    }


    @PatchMapping
    public EventFullDto updateEventByUser(@PathVariable("userId") @Positive Long userId,
                                          @RequestBody @Valid UpdateEventRequest request) {
        return eventService.updateEventByUser(userId, request);
    }


    @PostMapping
    public EventFullDto createEvent(@PathVariable("userId") @Positive Long userId,
                                    @RequestBody @Valid NewEventRequest request) {
        return eventService.createEventByUser(userId, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventOfUserById(@PathVariable("userId") @Positive Long userId,
                                           @PathVariable("eventId") @Positive Long eventId) {
        return eventService.getEventOfUserById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEventByUser(@PathVariable("userId") @Positive Long userId,
                                          @PathVariable("eventId") @Positive Long eventId) {
        return eventService.cancelEventByUser(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getAllRequestOfEvent(@PathVariable("userId") @Positive Long userId,
                                                              @PathVariable("eventId") @Positive Long eventId) {
        return eventService.getRequestsByEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{requestId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable("userId") @Positive Long userId,
                                                  @PathVariable("eventId") @Positive Long eventId,
                                                  @PathVariable("requestId") @Positive Long requestId) {
        return eventService.confirmRequest(userId, eventId, requestId);
    }

    @PatchMapping("/{eventId}/requests/{requestId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable("userId") @Positive Long userId,
                                                 @PathVariable("eventId") @Positive Long eventId,
                                                 @PathVariable("requestId") @Positive Long requestId) {
        return eventService.rejectRequest(userId, eventId, requestId);
    }
}
