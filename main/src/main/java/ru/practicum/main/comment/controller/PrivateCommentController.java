package ru.practicum.main.comment.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.request.NewCommentRequest;
import ru.practicum.main.comment.request.UpdateCommentRequest;
import ru.practicum.main.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class PrivateCommentController {

    private final CommentService service;

    @PostMapping("/events/{eventId}")
    public CommentDto createComment(@PathVariable("userId") @Positive Long userId,
                                    @PathVariable("eventId") @Positive Long eventId,
                                    @RequestBody @Valid NewCommentRequest request) {
        return service.createComment(request, userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public CommentDto updateComment(@PathVariable("userId") @Positive Long userId,
                                    @PathVariable("eventId") @Positive Long eventId,
                                    @RequestBody @Valid UpdateCommentRequest request) {
        return service.updateComment(request, userId, eventId);
    }

    @DeleteMapping("/{commentId}/events/{eventId}")
    public void deleteComment(@PathVariable("userId") @Positive Long userId,
                              @PathVariable("eventId") @Positive Long eventId,
                              @PathVariable("commentId") @Positive Long commentId) {
        service.deleteCommentByUser(userId, eventId, commentId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getComment(@PathVariable("userId") @Positive Long userId,
                                 @PathVariable("commentId") @Positive Long commentId) {
        return service.getComment(userId, commentId);
    }

    @GetMapping()
    public List<CommentDto> getComment(@PathVariable("userId") @Positive Long userId,
                                       @RequestParam(value = "from", defaultValue = "0")
                                       @PositiveOrZero Integer from,
                                       @RequestParam(value = "size", defaultValue = "10")
                                       @Positive Integer size) {
        return service.getAllComments(userId, from, size);
    }

}
