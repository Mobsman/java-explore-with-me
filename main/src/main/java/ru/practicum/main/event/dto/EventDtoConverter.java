package ru.practicum.main.event.dto;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.main.StatisticClient.EndpointHit;
import ru.practicum.main.StatisticClient.StatisticClient;
import ru.practicum.main.category.dto.CategoryDtoConverter;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.event.entity.Location;
import ru.practicum.main.user.dto.UserDtoConverter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventDtoConverter {

    private final CategoryDtoConverter categoryDtoConverter;
    private final UserDtoConverter userDtoConverter;
    private final StatisticClient statisticClient;


    public EventFullDto convertToEventFullDto(Event source) {

        Location location = new Location(source.getLat(), source.getLon());

        return EventFullDto.builder()
                .id(source.getId())
                .annotation(source.getAnnotation())
                .category(categoryDtoConverter.convertCategoryDto(source.getCategory()))
                .confirmedRequests(source.getConfirmedRequests())
                .createdOn(source.getCreatedOn())
                .description(source.getDescription())
                .eventDate(source.getEventDate())
                .initiator(userDtoConverter.convertShortDto(source.getInitiator()))
                .location(location)
                .paid(source.getPaid())
                .participantLimit(source.getParticipantLimit())
                .publishedOn(source.getPublishedOn())
                .requestModeration(source.getRequestModeration())
                .state(source.getState())
                .title(source.getTitle())
                .views(getViews(source.getId())).build();

    }

    public EventShortDto convertToEventShortDto(Event source) {

        return EventShortDto.builder()
                .id(source.getId())
                .annotation(source.getAnnotation())
                .category(categoryDtoConverter.convertCategoryDto(source.getCategory()))
                .confirmedRequests(source.getConfirmedRequests())
                .eventDate(source.getEventDate())
                .initiator(userDtoConverter.convertShortDto(source.getInitiator()))
                .paid(source.getPaid())
                .title(source.getTitle())
                .views(getViews(source.getId())).build();


    }

    public List<EventShortDto> toShortDtos(List<Event> events) {
        return events.stream().map(this::convertToEventShortDto).collect(Collectors.toList());
    }

    public Page<EventShortDto> convertPageToShortDto(Page<Event> page) {

        if (page.isEmpty()) {
            return Page.empty();
        }

        return new PageImpl<>(this.toShortDtos(page.getContent()), page.getPageable(), page.getTotalElements());
    }

    public List<EventShortDto> convertShortDtoLBySort(List<Event> events, String sort) {

        if (sort.equals(EventSort.EVENT_DATE.toString())) {
            return events.stream()
                    .map(this::convertToEventShortDto)
                    .sorted(Comparator.comparing(EventShortDto::getEventDate))
                    .collect(Collectors.toList());
        }

        if (sort.equals(EventSort.VIEWS.toString())) {
            return events.stream()
                    .map(this::convertToEventShortDto)
                    .sorted(Comparator.comparingLong(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }
        return events.stream()
                .map(this::convertToEventShortDto)
                .collect(Collectors.toList());

    }

    public long getViews(Long eventId) {

        ResponseEntity<Object> responseEntity = statisticClient.getStats(
                LocalDateTime.of(2022, 9, 1, 0, 0),
                LocalDateTime.now(),
                List.of("/events/" + eventId),
                false);

        List<Object> response = (List<Object>) responseEntity.getBody();

        ObjectMapper mapper = new ObjectMapper();
        List<EndpointHit> result = response.stream()
                .map(object -> mapper.convertValue(object, EndpointHit.class))
                .collect(Collectors.toList());
        if (result.isEmpty()) return 0;
        return result.get(0).getHits();
    }

}
