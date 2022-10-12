package ru.practicum.main.user.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {
    private String email;
    private String name;
    private Long id;
}
