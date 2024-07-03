package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmService {
    List<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Integer id);

    List<User> addLike(Integer id, Integer userId);

    List<User> removeLike(Integer id, Integer userId);

    List<Film> getPopularFilms(Integer count);
}
