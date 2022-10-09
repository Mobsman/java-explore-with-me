package ru.practicum.main.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.request.NewCategoryRequest;
import ru.practicum.main.category.request.UpdateCategoryRequest;
import ru.practicum.main.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@RestController
public class AdminCategoryController {
    private final CategoryService service;

    @PostMapping
    public CategoryDto create(@Valid @RequestBody NewCategoryRequest newCategoryDto) {
        return service.create(newCategoryDto);
    }

    @PatchMapping
    public CategoryDto update(@Valid @RequestBody UpdateCategoryRequest request) {
        return service.update(request);
    }

    @DeleteMapping("/{catId}")
    public void delete(@PathVariable @Positive Long catId) {
        service.delete(catId);
    }
}