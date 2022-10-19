package ru.practicum.main.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.exception.CategoryNotFoundException;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.dto.CommentDtoConverter;
import ru.practicum.main.comment.entity.Comment;
import ru.practicum.main.comment.entity.CommentState;
import ru.practicum.main.comment.exception.CommentAuthorException;
import ru.practicum.main.comment.exception.CommentNotFoundException;
import ru.practicum.main.comment.repository.CommentRepository;
import ru.practicum.main.comment.request.NewCommentRequest;
import ru.practicum.main.comment.request.UpdateCommentRequest;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.event.entity.EventState;
import ru.practicum.main.event.exception.EventNotFoundException;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.user.entity.User;
import ru.practicum.main.user.exception.UserNotFoundException;
import ru.practicum.main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentDtoConverter converter;


    public CommentDto createComment(NewCommentRequest request, Long userId, Long eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new EventNotFoundException("Событие не найдено"));
        User user = userRepository.findById(userId).orElseThrow(()
                -> new UserNotFoundException("Пользователь не найден"));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new EventNotFoundException("Событие не найдено");
        }

        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setState(CommentState.ON_MODERATION);
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
        comment.setState(CommentState.ON_MODERATION);

        return converter.toDto(commentRepository.save(comment));
    }


    public CommentDto getComment(Long userId, Long commentId) {

        if (commentRepository.findByIdAndAuthorId(commentId, userId) == null) {
            throw new CommentAuthorException("Комментарий принадлежит не вам");
        }

        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new CommentNotFoundException("Комментарий не найден"));

        return converter.toDto(comment);
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

        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new CategoryNotFoundException("Комментарий не найден"));

        comment.setText(request.getText());
        comment.setEdited(LocalDateTime.now());

        return converter.toDto(commentRepository.save(comment));
    }


    public CommentDto publishCommentByAdmin(Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new CategoryNotFoundException("Комментарий не найден"));

        comment.setState(CommentState.PUBLISHED);
        comment.setEdited(LocalDateTime.now());

        return converter.toDto(commentRepository.save(comment));
    }

    public CommentDto canceledCommentByAdmin(Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new CategoryNotFoundException("Комментарий не найден"));

        comment.setState(CommentState.CANCELED);
        comment.setEdited(LocalDateTime.now());

        return converter.toDto(commentRepository.save(comment));
    }

    @Transactional
    public void deleteAllComments(Long userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        commentRepository.deleteAllByAuthor_Id(userId);
    }

    public List<CommentDto> getAllComments(Long userId, int from, int size) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        Page<Comment> comments = commentRepository.findAllByAuthor_Id(userId, PageRequest.of(from / size, size));
        return comments.stream().map(converter::toDto).collect(Collectors.toList());
    }

    private void checkEventAndUserId(Long eventId, Long userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }

        if (eventRepository.findById(eventId).isEmpty()) {
            throw new EventNotFoundException("Событие не найдено");
        }
    }


}
