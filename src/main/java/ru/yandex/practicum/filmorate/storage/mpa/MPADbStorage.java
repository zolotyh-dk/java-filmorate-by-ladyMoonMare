package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mappers.MPARowMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MPADbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MPARowMapper mrm;

    public List<MPA> getAllRatings() {
        return jdbcTemplate.query("SELECT * FROM mpa", mrm);
    }

    public Optional<MPA> findRatingById(Integer id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE id =" +
                " ?;", mrm, id));
    }
}
