package ru.practicum.main.comment.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.request.NewCommentRequest;
import ru.practicum.main.comment.request.UpdateCommentRequest;
import ru.practicum.main.comment.service.CommentService;


@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class PrivateCommentController {

    private final CommentService service;

    @PostMapping("/events/{eventId}")
    public CommentDto createComment(@PathVariable("userId") Long userId,
                                    @PathVariable("eventId") Long eventId,
                                    @RequestBody NewCommentRequest request) {

        return service.createComment(request, userId, eventId);
    }

    @PutMapping("/events/{eventId}")
    public CommentDto updateComment(@PathVariable("userId") Long userId,
                                    @PathVariable("eventId") Long eventId,
                                    @RequestBody UpdateCommentRequest request) {
        return service.updateComment(request, userId, eventId);
    }

    @DeleteMapping("/{commentId}/events/{eventId}")
    public void deleteComment(@PathVariable("userId") Long userId,
                              @PathVariable("eventId") Long eventId,
                              @PathVariable("commentId") Long commentId) {
        service.deleteCommentByUser(userId, eventId, commentId);
    }


}
