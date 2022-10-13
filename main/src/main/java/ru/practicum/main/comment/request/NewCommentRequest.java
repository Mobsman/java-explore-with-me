package ru.practicum.main.comment.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentRequest {

    @NotNull
    @NotBlank
    private String text;
}

