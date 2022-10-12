package ru.practicum.main.compilation.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.service.CompilationService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(value = "pinned", defaultValue = "false")
                                       @NotNull Boolean pinned,
                                       @RequestParam(value = "from", defaultValue = "0")
                                       @PositiveOrZero Integer from,
                                       @RequestParam(value = "size", defaultValue = "10")
                                       @Positive Integer size) {

        return compilationService.getAll(pinned, from, size);
    }

    @GetMapping("/{compilationId}")
    public CompilationDto getById(@PathVariable("compilationId") @Positive Long compilationId) {
        return compilationService.getById(compilationId);
    }
}
