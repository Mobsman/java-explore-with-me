package ru.practicum.main.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.request.UpdateCommentRequest;
import ru.practicum.main.comment.service.CommentService;

import javax.validation.Valid;


@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService service;

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable("commentId") Long commentId) {
        service.deleteCommentByAdmin(commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto editComment(@PathVariable("commentId") Long commentId,
                                  @RequestBody @Valid UpdateCommentRequest request) {
        return service.editCommentByAdmin(commentId, request);
    }

}

