package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendsDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper urm;

    public List<User> getFriendsFromDb(Integer id) {
        return jdbcTemplate.query("SELECT * FROM app_users AS u JOIN friends AS f " +
                "ON u.id = f.friend_id WHERE user_id =?;",urm,id);
    }

    public void addFriend(Integer id, Integer friendId) {
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES (?,?);",id,friendId);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?",id,friendId);
    }
}
