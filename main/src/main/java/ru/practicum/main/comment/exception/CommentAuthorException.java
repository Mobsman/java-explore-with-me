package ru.practicum.main.comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CommentAuthorException extends RuntimeException {

    public CommentAuthorException(String message) {
        super(message);
    }
}