package ru.practicum.main.comment.dto;

import org.springframework.stereotype.Component;
import ru.practicum.main.comment.entity.Comment;

import java.time.LocalDateTime;


@Component
public class CommentDtoConverter {


    public CommentDto toDto(Comment source) {
        return CommentDto.builder()
                .id(source.getId())
                .author(source.getAuthor().getId())
                .event(source.getEvent().getId())
                .text(source.getText())
                .created(LocalDateTime.now())
                .edited(source.getEdited()).build();


    }
}
