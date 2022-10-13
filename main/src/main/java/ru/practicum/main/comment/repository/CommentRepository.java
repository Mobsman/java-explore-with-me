package ru.practicum.main.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.comment.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByIdAndAuthorId(Long commentId, Long userId);

    List<Comment> findByEventId(Long eventId);
}
