package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

public interface MPAStorage {
    List<MPA> getAllRatings();

    Optional<MPA> findRatingById(Integer id);
}
