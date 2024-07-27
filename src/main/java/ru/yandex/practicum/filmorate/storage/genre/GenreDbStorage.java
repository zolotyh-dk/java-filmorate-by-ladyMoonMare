package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcOperations jdbcTemplate;
    private final GenreRowMapper grm;

    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM genres", grm);
    }

    public Optional<Genre> findGenreById(Integer id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM genres WHERE id =" +
                    " ?;", grm, id));
        } catch (EmptyResultDataAccessException e) {
            log.warn("genre with id {} not found",id);
            throw  new DataNotFoundException("genre with id {} not found");
        }
    }

    @Override
    public List<Genre> getGenresByFilmId(Integer filmId) {
        return jdbcTemplate.query("SELECT * FROM genres AS g JOIN film_genre AS fg " +
                "ON g.id = fg.genre_id WHERE film_id = ?;", grm, filmId);
    }

    @Override
    public void addFilmGenre(Integer filmId, Integer genreId) {
        jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?,?);", filmId,
                genreId);
    }

    @Override
    public void removeFilmGenre(Integer filmId) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?;",filmId);
    }

    @Override
    public Integer getNumberOfGenres() {
        return jdbcTemplate.queryForObject("SELECT COUNT(id) FROM genres;",Integer.class);
    }
}
