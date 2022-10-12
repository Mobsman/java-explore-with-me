package ru.practicum.main.compilation.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main.compilation.entity.Compilation;
import ru.practicum.main.event.dto.EventDtoConverter;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationDtoConverter {

    private final EventDtoConverter converter;

    public CompilationDto toDto(Compilation source) {
        return CompilationDto.builder()
                .id(source.getId())
                .events(source.getEvents().stream()
                        .map(converter::convertToEventShortDto)
                        .collect(Collectors.toList()))
                .pinned(source.getPinned())
                .title(source.getTitle()).build();
    }

}
