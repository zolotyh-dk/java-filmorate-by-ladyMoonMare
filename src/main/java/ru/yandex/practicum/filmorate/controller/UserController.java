package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("addUser attempt {}",user);
        validateUser(user);
        userService.addUser(user);
        log.info("addUser {} success",user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("updateUser attempt {}",user);
        validateUser(user);
        userService.updateUser(user);
        return user;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        log.info("attempt to get user by id {}",id);
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Integer id) {
        log.info("attempt to get user friend list by id {}",id);
        return userService.getUserFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<User> addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("attempt to add user {} to user's {} friend list", friendId, id);
        userService.addFriend(id, friendId);
        return userService.getUserFriends(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<User> deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("attempt to delete user {} from user's {} friend list", friendId, id);
        userService.deleteFriend(id, friendId);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("attempt to find user's {} and user's {} common friends", otherId, id);
        return userService.getCommonFriends(id, otherId);
    }

    public void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Data error - invalid login {}",user.getLogin());
            throw new ValidationException("Invalid login");
        } else if (user.getBirthday().isAfter(LocalDate.now()) ||
                user.getBirthday().isEqual(LocalDate.now()) || user.getBirthday() == null) {
            log.warn("Data error - invalid birthday {}",user.getBirthday());
            throw new ValidationException("Invalid birthday");
        }
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            log.info("Username is empty, login becomes username");
            user.setName(user.getLogin());
        }
    }
}
