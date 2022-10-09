package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.EndpointDto;
import ru.practicum.stats.dto.EndpointDtoConverter;
import ru.practicum.stats.dto.StatisticDto;
import ru.practicum.stats.dto.EndpointRequest;
import ru.practicum.stats.entity.EndpointHit;
import ru.practicum.stats.repository.EndpointRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final EndpointRepository repository;
    private final EndpointDtoConverter converter;

    public EndpointDto create(EndpointRequest request) {

        EndpointHit endpoint = new EndpointHit();
        endpoint.setApp(request.getApp());
        endpoint.setUri(request.getUri());
        endpoint.setIp(request.getIp());
        endpoint.setTime(LocalDateTime.now());

        return converter.converter(repository.save(endpoint));
    }

    public List<StatisticDto> get(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {

        if (uris == null) {
            return Collections.emptyList();
        }

        List<StatisticDto> stats = new ArrayList<>();


        for (String uri : uris) {
            List<EndpointHit> forUriResult = repository.findByUriAndTimeAfterAndTimeBefore(uri, start, end);
            if (!forUriResult.isEmpty()) {
                StatisticDto statisticDto = StatisticDto.builder()
                        .app(forUriResult.get(0).getApp())
                        .uri(uri)
                        .hits(forUriResult.size())
                        .build();
                if (unique) {
                    long hits = forUriResult.stream()
                            .map(EndpointHit::getIp)
                            .distinct()
                            .count();
                    statisticDto.setHits(hits);
                }
                stats.add(statisticDto);
            }
        }
        return stats;
    }
}
