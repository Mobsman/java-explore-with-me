package ru.practicum.main.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;


@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ParticipationRequestDto createRequest(@PathVariable("userId") @Positive Long userId,
                                                 @RequestParam("eventId") @Positive Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUser(@PathVariable("userId") @Positive Long userId,
                                                       @PathVariable("requestId") @Positive Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getAllByUser(@PathVariable("userId") @Positive Long userId) {
        return requestService.getAllByUser(userId);
    }
}
