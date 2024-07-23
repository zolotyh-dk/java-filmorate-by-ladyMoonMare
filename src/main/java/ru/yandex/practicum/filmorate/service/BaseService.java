package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface BaseService<T> {
    List<T> getAll();

    T getById(Integer id);
}
