package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stats.entity.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointRepository extends JpaRepository<EndpointHit, Long> {

    List<EndpointHit> findByUriAndTimeAfterAndTimeBefore(String uri, LocalDateTime start, LocalDateTime end);
}
