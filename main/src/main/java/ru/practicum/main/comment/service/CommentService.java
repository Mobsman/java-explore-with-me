package ru.practicum.main.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main.category.exception.CategoryNotFoundException;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.dto.CommentDtoConverter;
import ru.practicum.main.comment.entity.Comment;
import ru.practicum.main.comment.exception.CommentAuthorException;
import ru.practicum.main.comment.exception.CommentNotFoundException;
import ru.practicum.main.comment.repository.CommentRepository;
import ru.practicum.main.comment.request.NewCommentRequest;
import ru.practicum.main.comment.request.UpdateCommentRequest;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.event.exception.EventNotFoundException;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.user.entity.User;
import ru.practicum.main.user.exception.UserNotFoundException;
import ru.practicum.main.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentDtoConverter converter;


    public CommentDto createComment(NewCommentRequest request, Long userId, Long eventId) {

        Comment comment = new Comment();

        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new EventNotFoundException("Событие не найдено"));
        User user = userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException("Пользователь не найден"));

        comment.setText(request.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setEvent(event);
        comment.setAuthor(user);

        return converter.toDto(commentRepository.save(comment));
    }


    public CommentDto updateComment(UpdateCommentRequest request, Long userId, Long eventId) {

        checkEventAndUserId(eventId, userId);

        if (commentRepository.findByIdAndAuthorId(request.getId(), userId) == null) {
            throw new CommentAuthorException("Комментарий принадлежит не вам");
        }

        Comment comment = commentRepository.findById(request.getId()).orElseThrow(()
                -> new CategoryNotFoundException("Категория не найдена"));

        comment.setText(request.getText());
        comment.setEdited(LocalDateTime.now());

        return converter.toDto(commentRepository.save(comment));
    }


    public void deleteCommentByUser(Long userId, Long eventId, Long commentId) {

        checkEventAndUserId(eventId, userId);

        if (commentRepository.findById(commentId).isEmpty()) {
            throw new CommentNotFoundException("Комментарий не найден");
        }

        if (commentRepository.findByIdAndAuthorId(commentId, userId) == null) {
            throw new CommentAuthorException("Комментарий принадлежит не вам");
        }

        commentRepository.deleteById(commentId);
    }


    public void deleteCommentByAdmin(Long commentId) {


        if (commentRepository.findById(commentId).isEmpty()) {
            throw new CommentNotFoundException("Комментарий не найден");
        }

        commentRepository.deleteById(commentId);
    }

    public CommentDto editCommentByAdmin(Long commentId, UpdateCommentRequest request) {


        if (commentRepository.findById(commentId).isEmpty()) {
            throw new CommentNotFoundException("Комментарий не найден");
        }

        Comment comment = commentRepository.findById(request.getId()).orElseThrow(()
                -> new CategoryNotFoundException("Категория не найдена"));

        comment.setText(request.getText());
        comment.setEdited(LocalDateTime.now());

        return converter.toDto(commentRepository.save(comment));
    }


    public void checkEventAndUserId(Long eventId, Long userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (eventRepository.findById(eventId).isEmpty()) {
            throw new EventNotFoundException("Событие не найдено");
        }


    }
}
