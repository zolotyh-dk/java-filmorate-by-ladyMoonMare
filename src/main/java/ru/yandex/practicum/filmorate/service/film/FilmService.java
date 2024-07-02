package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmService {
    List<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Integer id);
}
