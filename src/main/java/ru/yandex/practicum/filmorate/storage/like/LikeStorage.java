package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface LikeStorage {
    List<User> getLikesFromDb(Integer id);

    void addLike(Integer id, Integer userId);

    void removeLike(Integer id, Integer userId);
}
