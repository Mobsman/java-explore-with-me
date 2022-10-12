package ru.practicum.main.category.dto;

import org.springframework.stereotype.Component;
import ru.practicum.main.category.entity.Category;

@Component
public class CategoryDtoConverter {


    public CategoryDto convertCategoryDto(Category source) {
        return CategoryDto.builder()
                .id(source.getId())
                .name(source.getName()).build();
    }

}
