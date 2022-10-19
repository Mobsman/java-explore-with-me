package ru.practicum.main.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.comment.entity.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByIdAndAuthorId(Long commentId, Long userId);

    Page<Comment> findAllByAuthor_Id(Long userId, Pageable pageable);

    void deleteAllByAuthor_Id(Long userId);

}
