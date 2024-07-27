package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mappers.MPARowMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MPADbStorage implements MPAStorage {
    private final JdbcOperations jdbcTemplate;
    private final MPARowMapper mrm;

    @Override
    public List<MPA> getAllRatings() {
        return jdbcTemplate.query("SELECT * FROM mpa", mrm);
    }

    @Override
    public Optional<MPA> findRatingById(Integer id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE mpa_id =" +
                    " ?;", mrm, id));
        } catch (EmptyResultDataAccessException e) {
            log.warn("mpa with id {} not found",id);
            throw  new DataNotFoundException("mpa with id {} not found");
        }
    }

    @Override
    public Integer getNumberOfMpa() {
        return jdbcTemplate.queryForObject("SELECT COUNT(id) FROM mpa;",Integer.class);
    }
}
