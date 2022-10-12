package ru.practicum.main.category.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class CategoryUniqException extends RuntimeException {

    public CategoryUniqException(String message) {
        super(message);
    }
}
