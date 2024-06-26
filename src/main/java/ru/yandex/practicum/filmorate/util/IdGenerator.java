package ru.yandex.practicum.filmorate.util;

import lombok.Getter;

@Getter
public class IdGenerator {
    private Integer id = hashCode();
}
