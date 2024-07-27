package ru.yandex.practicum.filmorate.service;

import java.util.List;

public interface BaseService<T> {
    List<T> getAll();

    T getById(Integer id);

    Integer getNumberOf();
}
