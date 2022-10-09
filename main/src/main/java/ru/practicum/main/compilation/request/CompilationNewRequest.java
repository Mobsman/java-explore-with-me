package ru.practicum.main.compilation.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CompilationNewRequest {

    @NotEmpty
    @NotNull
    private String title;
    @NotNull
    private Boolean pinned;
    @NotNull
    private List<Long> events;

}