package ru.practicum.main.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.main.user.entity.User;


@Component
public class UserDtoConverter {


    public UserDto convertFullDto(User source) {
        return UserDto.builder()
                .id(source.getId())
                .name(source.getName())
                .email(source.getEmail()).build();
    }

    public UserShortDto convertShortDto(User source) {
        return UserShortDto.builder()
                .id(source.getId())
                .name(source.getName()).build();
    }

}
