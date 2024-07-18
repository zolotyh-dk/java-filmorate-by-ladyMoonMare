package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query("SELECT * FROM films;",filmRowMapper);
    }

    @Override
    public Film addFilm(Film film) {
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Optional<Film> findFilmById(Integer id) {
        return Optional.empty();
    }
}
