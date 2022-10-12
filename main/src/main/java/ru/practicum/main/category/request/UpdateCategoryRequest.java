package ru.practicum.main.category.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class UpdateCategoryRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String name;
}
