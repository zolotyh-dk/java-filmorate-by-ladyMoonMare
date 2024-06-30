package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidDataException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private IdGenerator idGen = new IdGenerator();


    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("addUser attempt {}",user);
        users.values()
                .forEach(u -> {
                    if (u.getLogin().equals(user.getLogin())) {
                        log.warn("addUser attempt failure - user {} is already registered",user);
                        throw new InvalidDataException("User is already registered");
                    }
                });
        if (users.containsValue(user)) {
            log.warn("addUser attempt failure - user {} is already registered",user);
            throw new InvalidDataException("User is already registered");
        }
        validateUser(user);
        user.setId(idGen.getId());
        idGen.reloadId();
        users.put(user.getId(),user);
        log.info("addUser {} success",user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("updateUser attempt {}",user);
        if (!users.containsKey(user.getId())) {
            log.warn("updateUser failure - user {} has not been registered", user);
            throw new InvalidDataException("User has not been registered");
        }
        validateUser(user);
        users.put(user.getId(),user);
        log.info("updateUser {} success", user);
        return user;
    }

    public void validateUser(User user) {
        if (user.getEmail().isEmpty() || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Data error - invalid email {}",user.getEmail());
            throw new InvalidDataException("Invalid email");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Data error - invalid login {}",user.getLogin());
            throw new InvalidDataException("Invalid login");
        } else if (user.getBirthday().isAfter(LocalDate.now()) ||
                user.getBirthday().isEqual(LocalDate.now()) || user.getBirthday() == null) {
            log.warn("Data error - invalid birthday {}",user.getBirthday());
            throw new InvalidDataException("Invalid birthday");
        }
        if (user.getName().isEmpty() || user.getName().isBlank() || user.getName() == null) {
            log.info("Username is empty, login is username");
            user.setName(user.getLogin());
        }
    }
}
