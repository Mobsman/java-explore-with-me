package ru.practicum.main.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.request.UpdateCommentRequest;
import ru.practicum.main.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;


@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService service;

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable("commentId") @Positive Long commentId) {
        service.deleteCommentByAdmin(commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto editComment(@PathVariable("commentId") @Positive Long commentId,
                                  @RequestBody @Valid UpdateCommentRequest request) {
        return service.editCommentByAdmin(commentId, request);
    }

    @PatchMapping("/{commentId}/publish")
    public CommentDto publishComment(@PathVariable("commentId") @Positive Long commentId) {
        return service.publishCommentByAdmin(commentId);
    }

    @PatchMapping("/{commentId}/cancel")
    public CommentDto cancelComment(@PathVariable("commentId") @Positive Long commentId) {
        return service.canceledCommentByAdmin(commentId);
    }

    @DeleteMapping("/user/{userId}")
    public void deleteAllComments(@PathVariable("userId") @Positive Long userId) {
        service.deleteAllComments(userId);
    }

}

