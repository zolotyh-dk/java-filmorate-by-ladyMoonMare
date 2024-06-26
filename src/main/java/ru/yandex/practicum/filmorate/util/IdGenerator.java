package ru.yandex.practicum.filmorate.util;

import lombok.Getter;

@Getter
public class IdGenerator {
    private Integer id = 1;

    public void reloadId() {
        id++;
    }
}
