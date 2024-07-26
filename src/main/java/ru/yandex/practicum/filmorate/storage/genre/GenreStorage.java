package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<Genre> getAllGenres();

    Optional<Genre> findGenreById(Integer id);

    List<Genre> getGenresByFilmId(Integer filmId);

    void addFilmGenre(Integer filmId, Integer genreId);

    void removeFilmGenre(Integer filmId);
}
