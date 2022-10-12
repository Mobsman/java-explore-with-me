package ru.practicum.main.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.StatisticClient.EndpointRequest;
import ru.practicum.main.StatisticClient.StatisticClient;
import ru.practicum.main.event.dto.EventDtoConverter;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.event.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final PublicEventService publicEventService;
    private final EventDtoConverter converter;
    private final StatisticClient statisticClient;


    @GetMapping
    public List<EventShortDto> searchEvents(@RequestParam(value = "text", required = false)
                                                    String text,
                                            @RequestParam(value = "categories", required = false)
                                                    List<Long> categories,
                                            @RequestParam(value = "paid", required = false)
                                                    Boolean paid,
                                            @RequestParam(value = "rangeStart", required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                    LocalDateTime rangeStart,
                                            @RequestParam(value = "rangeEnd", required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                    LocalDateTime rangeEnd,
                                            @RequestParam(name = "sort", defaultValue = "EVENT_DATE") String sort,
                                            @RequestParam(value = "onlyAvailable", required = false)
                                                    Boolean onlyAvailable,
                                            @RequestParam(value = "from", defaultValue = "0")
                                            @PositiveOrZero Integer from,
                                            @RequestParam(value = "size", defaultValue = "10")
                                            @Positive Integer size, HttpServletRequest request) {

        EndpointRequest endpoint = new EndpointRequest(
                "explore-with-me",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        );

        statisticClient.save(endpoint);

        List<Event> events = publicEventService.searchEvents(text, categories, paid, rangeStart, rangeEnd, from, size);

        return converter.convertShortDtoLBySort(events, sort);

    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(@PathVariable("eventId") @Positive Long eventId, HttpServletRequest request) {

        EndpointRequest endpoint = new EndpointRequest(
                "explore-with-me",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        );

        statisticClient.save(endpoint);

        return publicEventService.getEventById(eventId);
    }
}
