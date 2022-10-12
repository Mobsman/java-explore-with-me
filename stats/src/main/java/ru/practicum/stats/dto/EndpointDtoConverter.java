package ru.practicum.stats.dto;

import org.springframework.stereotype.Component;
import ru.practicum.stats.entity.EndpointHit;

@Component
public class EndpointDtoConverter {


    public EndpointDto converter(EndpointHit source) {
        return EndpointDto.builder()
                .id(source.getId())
                .app(source.getApp())
                .ip(source.getIp())
                .uri(source.getUri())
                .timestamp(source.getTime()).build();
    }
}
