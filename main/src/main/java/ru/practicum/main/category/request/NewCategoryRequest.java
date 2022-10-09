package ru.practicum.main.category.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NewCategoryRequest {

    @NotNull
    @NotEmpty
    private String name;

}
