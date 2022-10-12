package ru.practicum.main.compilation.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.request.CompilationNewRequest;
import ru.practicum.main.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;


@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto createNewCompilation(@RequestBody @Valid CompilationNewRequest request) {

        return compilationService.createNewCompilation(request);
    }

    @DeleteMapping("/{compilationId}")
    public void deleteCompilation(@PathVariable("compilationId") @Positive Long compilationId) {
        compilationService.deleteCompilation(compilationId);
    }

    @DeleteMapping("/{compilationId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable("compilationId") @Positive Long compilationId,
                                           @PathVariable("eventId") @Positive Long eventId) {
        compilationService.deleteEventFromCompilation(compilationId, eventId);
    }

    @PatchMapping("/{compilationId}/events/{eventId}")
    public CompilationDto addEventToCompilation(@PathVariable("compilationId") @Positive
                                                        Long compilationId,
                                                @PathVariable("eventId") @Positive Long eventId) {
        return compilationService.addEventFromCompilation(compilationId, eventId);
    }

    @DeleteMapping("/{compilationId}/pin")
    public void unpinCompilation(@PathVariable("compilationId") @Positive Long compilationId) {
        compilationService.unpinCompilation(compilationId);
    }

    @PatchMapping("/{compilationId}/pin")
    public CompilationDto pinCompilation(@PathVariable("compilationId") @Positive Long compilationId) {
        return compilationService.pinCompilation(compilationId);
    }
}
