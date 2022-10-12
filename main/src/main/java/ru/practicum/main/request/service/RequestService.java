package ru.practicum.main.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.event.entity.EventState;
import ru.practicum.main.event.exception.EventNotFoundException;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.request.dto.ParticipationDtoConverter;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.request.entity.Request;
import ru.practicum.main.request.entity.RequestStatus;
import ru.practicum.main.request.exception.RequestException;
import ru.practicum.main.request.reposirory.RequestRepository;
import ru.practicum.main.user.exception.UserNotFoundException;
import ru.practicum.main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationDtoConverter converter;


    public ParticipationRequestDto createRequest(Long userId, Long eventId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (eventRepository.findById(eventId).isEmpty()) {
            throw new EventNotFoundException("Событие не найдено");
        }

        if (requestRepository.findByRequesterIdAndEventId(userId, eventId) != null) {
            throw new RequestException("Такой запрос уже существует");
        }

        Event event = eventRepository.findById(eventId).get();

        if (event.getInitiator().getId().equals(userId)) {
            throw new RequestException("Участник является автором события");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestException("Событие не публичное");
        }

        boolean limit = event.getParticipantLimit() != 0 &&
                event.getParticipantLimit() == requestRepository.findByEventIdAndStatus(eventId, RequestStatus.CONFIRMED)
                        .size();

        if (limit) {
            throw new RequestException("Лимит участников превышен");
        }

        Request request = new Request();

        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        request.setRequester(userRepository.findById(userId).get());
        request.setCreated(LocalDateTime.now());
        request.setEvent(eventRepository.findById(eventId).get());

        return converter.toDto(requestRepository.save(request));
    }


    public List<ParticipationRequestDto> getAllByUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return requestRepository.findByRequesterId(userId).stream().map(converter::toDto).collect(Collectors.toList());
    }


    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (requestRepository.findById(requestId).isEmpty()) {
            throw new RequestException("Запрос не найден");
        }

        Request request = requestRepository.findById(requestId).get();

        request.setStatus(RequestStatus.CANCELED);

        return converter.toDto(requestRepository.save(request));
    }
}
