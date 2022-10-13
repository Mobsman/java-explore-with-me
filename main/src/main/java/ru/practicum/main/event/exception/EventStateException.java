package ru.practicum.main.event.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EventStateException extends RuntimeException {

    public EventStateException(String message) {
        super(message);
    }
}
