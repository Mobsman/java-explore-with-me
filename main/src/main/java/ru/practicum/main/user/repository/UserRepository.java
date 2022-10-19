package ru.practicum.main.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.user.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {

}
