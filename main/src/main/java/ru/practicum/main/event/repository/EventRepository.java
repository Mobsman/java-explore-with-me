package ru.practicum.main.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.event.entity.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByInitiatorId(Long userId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

    Event findByIdAndState(Long eventId, EventState state);

    @Query("FROM event e " +
            "WHERE (upper(e.annotation) LIKE UPPER(concat('%', ?1, '%')) OR UPPER(e.description)" +
            " LIKE UPPER(concat('%', ?1, '%')))" +
            " AND e.category.id IN ?2 AND e.paid = ?3 AND e.eventDate > ?4 AND e.eventDate < ?5 AND e.state = ?6")
    List<Event> findEvents(String text, List<Long> categories, boolean paid,
                           LocalDateTime rangeStart, LocalDateTime rangeEnd, EventState state, Pageable pageable);


    @Query("from event e where e.initiator.id in ?1 and e.state in ?2" +
            " and e.category.id in ?3 and e.eventDate >= ?4 and e.eventDate <= ?5")
    Page<Event> findWithAdminParameters(List<Long> userIds, List<EventState> states, List<Long> categoryIds,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
