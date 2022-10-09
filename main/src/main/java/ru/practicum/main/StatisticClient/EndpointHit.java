package ru.practicum.main.StatisticClient;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {
    private String app;
    private String uri;
    private long hits;
}
