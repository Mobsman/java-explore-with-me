package ru.practicum.main.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.main.request.entity.Request;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParticipationDtoConverter {

    public  ParticipationRequestDto toDto(Request source) {

        return ParticipationRequestDto.builder()
                .id(source.getId())
                .created(source.getCreated())
                .event(source.getEvent().getId())
                .requester(source.getRequester().getId())
                .status(source.getStatus()).build();

    }

    public List<ParticipationRequestDto> toDtos(List<Request> requests) {
        return requests.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
