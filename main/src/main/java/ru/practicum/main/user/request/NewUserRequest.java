package ru.practicum.main.user.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class NewUserRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;


}
