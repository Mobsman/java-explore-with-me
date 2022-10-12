package ru.practicum.stats.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointDto;
import ru.practicum.stats.dto.StatisticDto;
import ru.practicum.stats.dto.EndpointRequest;
import ru.practicum.stats.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticController {


    private final StatisticService service;

    @PostMapping("/hit")
    public EndpointDto create(@RequestBody EndpointRequest request) {
        return service.create(request);
    }

    @GetMapping("/stats")
    public List<StatisticDto> get(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                  @RequestParam(value = "start") LocalDateTime start,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                  @RequestParam(value = "end") LocalDateTime end,
                                  @RequestParam String[] uris,
                                  @RequestParam(defaultValue = "false") Boolean unique) {
        return service.get(start, end, uris, unique);
    }


}
