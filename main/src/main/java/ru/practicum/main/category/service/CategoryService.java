package ru.practicum.main.category.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.CategoryDtoConverter;
import ru.practicum.main.category.exception.CategoryUniqException;
import ru.practicum.main.category.request.NewCategoryRequest;
import ru.practicum.main.category.entity.Category;
import ru.practicum.main.category.exception.CategoryNotFoundException;
import ru.practicum.main.category.request.UpdateCategoryRequest;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class CategoryService {

    private final CategoryDtoConverter converter;

    private final CategoryRepository repository;


    public List<CategoryDto> getAll(Integer from, Integer size) {

        Page<Category> categoryPage = repository.findAll(PageRequest.of(from / size, size));

        return categoryPage.stream().map(converter::convertCategoryDto).collect(Collectors.toList());
    }

    public CategoryDto getById(long categoryId) {
        return converter.convertCategoryDto(
                repository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Категория не найдена")));
    }


    public CategoryDto create(NewCategoryRequest request) {

        if (repository.findByName(request.getName()).isPresent()) {
            throw new CategoryUniqException("Такая категория уже существует");
        }

        Category newCategory = new Category();
        newCategory.setName(request.getName());

        return converter.convertCategoryDto(repository.save(newCategory));

    }


    public CategoryDto update(UpdateCategoryRequest request) {

        if (repository.findById(request.getId()).isEmpty()) {
            throw new CategoryNotFoundException("Категория не найдена");
        }

        if (repository.findByName(request.getName()).isPresent()) {
            throw new CategoryUniqException("Категория не найдена");
        }

        Category category = repository.findById(request.getId()).get();

        category.setName(request.getName());

        return converter.convertCategoryDto(repository.save(category));
    }

    public void delete(Long categoryId) {

        if (repository.findById(categoryId).isEmpty()) {
            throw new CategoryNotFoundException("Категория не найдена");
        }
        repository.deleteById(categoryId);
    }


}
