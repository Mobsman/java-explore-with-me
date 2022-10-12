package ru.practicum.main.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.category.exception.CategoryNotFoundException;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.event.dto.EventDtoConverter;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.event.entity.EventState;
import ru.practicum.main.event.exception.EventNotFoundException;
import ru.practicum.main.event.exception.EventStateException;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.event.request.NewEventRequest;
import ru.practicum.main.event.request.UpdateEventRequest;
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

@Service
@RequiredArgsConstructor
public class PrivateEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final EventDtoConverter converter;
    private final ParticipationDtoConverter participationDtoConverter;


    public Page<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        Page<Event> events = eventRepository.findByInitiatorId(userId, PageRequest.of(from / size, size));

        return converter.convertPageToShortDto(events);
    }

    public EventFullDto updateEventByUser(Long userId, UpdateEventRequest request) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (eventRepository.findByIdAndInitiatorId(request.getEventId(), userId) == null) {
            throw new EventNotFoundException("Событие не найдено");
        }

        if (eventRepository.findById(request.getEventId()).isEmpty()) {
            throw new EventNotFoundException("Событие не найдено");
        }

        Event event = eventRepository.findById(request.getEventId()).get();

        event.setAnnotation(request.getAnnotation());
        if (request.getCategoryId() != null) {
            event.setCategory(categoryRepository.findById(request.getCategoryId()).get());
        }
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setPaid(request.getPaid());
        event.setParticipantLimit(request.getParticipantLimit());
        event.setTitle(request.getTitle());

        return converter.convertToEventFullDto(eventRepository.save(event));
    }

    public EventFullDto createEventByUser(Long userId, NewEventRequest request) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        if (categoryRepository.findById(request.getCategory()).isEmpty()) {
            throw new CategoryNotFoundException("Категория не найдена");
        }

        Event event = new Event();

        event.setAnnotation(request.getAnnotation());
        event.setCategory(categoryRepository.findById(request.getCategory()).get());
        event.setDescription(request.getDescription());
        event.setCreatedOn(LocalDateTime.now());
        event.setEventDate(request.getEventDate());
        event.setPaid(request.getPaid());
        event.setParticipantLimit(request.getParticipantLimit());
        event.setRequestModeration(request.getRequestModeration());
        event.setTitle(request.getTitle());
        event.setInitiator(userRepository.findById(userId).get());
        event.setState(EventState.PENDING);
        event.setLat(request.getLocation().getLat());
        event.setLon(request.getLocation().getLon());

        return converter.convertToEventFullDto(eventRepository.save(event));
    }


    public EventFullDto getEventOfUserById(Long userId, Long eventId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (eventRepository.findByIdAndInitiatorId(eventId, userId) == null) {
            throw new EventNotFoundException("Событие не найдено");
        }

        return converter.convertToEventFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId));
    }

    public EventFullDto cancelEventByUser(Long userId, Long eventId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (eventRepository.findByIdAndInitiatorId(eventId, userId) == null) {
            throw new EventNotFoundException("Событие не найдено");
        }

        if (eventRepository.findById(eventId).isEmpty()) {
            throw new EventNotFoundException("Событие не найдено");
        }

        Event event = eventRepository.findById(eventId).get();

        if (!event.getState().equals(EventState.PENDING)) {
            throw new EventStateException("Неверный статус");
        }

        event.setState(EventState.CANCELED);

        return converter.convertToEventFullDto(eventRepository.save(event));
    }

    public List<ParticipationRequestDto> getRequestsByEvent(Long userId, Long eventId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (eventRepository.findById(eventId).isEmpty()) {
            throw new EventNotFoundException("Событие не найдено");
        }

        if (eventRepository.findByIdAndInitiatorId(eventId, userId) == null) {
            throw new EventNotFoundException("Событие не найдено");
        }

        return participationDtoConverter.toDtos(requestRepository.findByEventId(eventId));
    }

    public ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long requestId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (eventRepository.findById(eventId).isEmpty()) {
            throw new EventNotFoundException("Пользователь не найден");
        }

        if (requestRepository.findById(requestId).isEmpty()) {
            throw new RequestException("Запрос не найден");
        }

        Request request = requestRepository.findById(requestId).get();

        request.setStatus(RequestStatus.CONFIRMED);

        return participationDtoConverter.toDto(requestRepository.save(request));
    }

    public ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long requestId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (eventRepository.findById(eventId).isEmpty()) {
            throw new EventNotFoundException("Пользователь не найдене");
        }

        if (requestRepository.findById(requestId).isEmpty()) {
            throw new RequestException("Запрос не найден");
        }

        Request request = requestRepository.findById(requestId).get();

        request.setStatus(RequestStatus.REJECTED);

        return participationDtoConverter.toDto(requestRepository.save(request));
    }
}
