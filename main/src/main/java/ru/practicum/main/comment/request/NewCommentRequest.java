package ru.practicum.main.comment.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentRequest {
    private String text;
}

