package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
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
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM genres WHERE id =" +
                " ?;", grm, id));
    }
}
