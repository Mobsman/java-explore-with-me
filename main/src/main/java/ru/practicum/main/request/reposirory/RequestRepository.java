package ru.practicum.main.request.reposirory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.request.entity.Request;
import ru.practicum.main.request.entity.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Request findByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findByRequesterId(Long userId);

    List<Request> findByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findByEventId(Long eventId);
}
