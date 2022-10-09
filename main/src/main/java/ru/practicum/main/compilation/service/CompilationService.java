package ru.practicum.main.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.exception.CategoryNotFoundException;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.CompilationDtoConverter;
import ru.practicum.main.compilation.entity.Compilation;
import ru.practicum.main.compilation.repository.CompilationRepository;
import ru.practicum.main.compilation.request.CompilationNewRequest;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.event.exception.EventNotFoundException;
import ru.practicum.main.event.repository.EventRepository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationDtoConverter converter;


    public CompilationDto createNewCompilation(CompilationNewRequest request) {

        Compilation compilation = new Compilation();

        compilation.setEvents(request.getEvents().stream()
                .map(eventRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));
        compilation.setPinned(request.getPinned());
        compilation.setTitle(request.getTitle());

        return converter.toDto(compilationRepository.save(compilation));

    }

    @Transactional
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {

        Page<Compilation> compilationPage = compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size));

        return compilationPage.stream().map(converter::toDto).collect(Collectors.toList());

    }

    @Transactional
    public CompilationDto getById(Long compilationId) {

        if (compilationRepository.findById(compilationId).isEmpty()) {
            throw new CategoryNotFoundException("Подборка не найдена");
        }
        return converter.toDto(compilationRepository.findById(compilationId).get());
    }

    public void deleteCompilation(Long compilationId) {
        if (compilationRepository.findById(compilationId).isEmpty()) {
            throw new CategoryNotFoundException("Подборка не найдена");
        }
        compilationRepository.deleteById(compilationId);
    }

    @Transactional
    public void deleteEventFromCompilation(Long compilationId, Long eventId) {
        if (compilationRepository.findById(compilationId).isEmpty()) {
            throw new CategoryNotFoundException("Подборка не найдена");
        }

        if (eventRepository.findById(eventId).isEmpty()) {
            throw new EventNotFoundException("Событие не найдено");
        }

        Event event = eventRepository.findById(eventId).get();
        Compilation compilation = compilationRepository.findById(compilationId).get();

        compilation.getEvents().remove(event);

        compilationRepository.save(compilation);

    }

    @Transactional
    public CompilationDto addEventFromCompilation(Long compilationId, Long eventId) {

        if (compilationRepository.findById(compilationId).isEmpty()) {
            throw new CategoryNotFoundException("Подборка не найдена");
        }

        if (eventRepository.findById(eventId).isEmpty()) {
            throw new EventNotFoundException("Событие не найдено");
        }

        Event event = eventRepository.findById(eventId).get();
        Compilation compilation = compilationRepository.findById(compilationId).get();

        compilation.getEvents().add(event);

        return converter.toDto(compilationRepository.save(compilation));
    }


    public void unpinCompilation(Long compilationId) {
        if (compilationRepository.findById(compilationId).isEmpty()) {
            throw new CategoryNotFoundException("Подборка не найдена");
        }

        Compilation compilation = compilationRepository.findById(compilationId).get();

        compilation.setPinned(false);

        compilationRepository.save(compilation);

    }

    @Transactional
    public CompilationDto pinCompilation(Long compilationId) {

        if (compilationRepository.findById(compilationId).isEmpty()) {
            throw new CategoryNotFoundException("Подборка не найдена");
        }

        Compilation compilation = compilationRepository.findById(compilationId).get();

        compilation.setPinned(true);

        return converter.toDto(compilationRepository.save(compilation));
    }
}


