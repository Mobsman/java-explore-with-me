package ru.practicum.main.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.user.entity.User;
import ru.practicum.main.user.repository.UserRepository;
import ru.practicum.main.user.request.NewUserRequest;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.dto.UserDtoConverter;
import ru.practicum.main.user.exception.UserNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoConverter userDtoConverter;


    public UserDto create(NewUserRequest request) {


        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());

        return userDtoConverter.convertFullDto(userRepository.save(newUser));
    }

    public void delete(long userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        userRepository.deleteById(userId);
    }


    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {

        List<User> users = ids.stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<User> page = users.stream()
                .sorted(Comparator.comparing(User::getId))
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());

        return page.stream().map(userDtoConverter::convertFullDto).collect(Collectors.toList());
    }

}
