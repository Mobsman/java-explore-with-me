package ru.practicum.main.user.adminController;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.user.service.UserService;
import ru.practicum.main.user.request.NewUserRequest;
import ru.practicum.main.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;


@RequestMapping("/admin/users")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminUserController {

    private final UserService service;


    @PostMapping
    public UserDto create(@Valid @RequestBody NewUserRequest user) {
        return service.create(user);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        service.delete(userId);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam("ids") List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        return service.getUsers(ids, from, size);
    }


}
