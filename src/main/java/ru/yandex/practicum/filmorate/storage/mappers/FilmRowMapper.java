package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("title"))
                .description(rs.getString("description"))
                .releaseDate(LocalDate.parse(rs.getString("releaseDate")))
                .duration(Duration.parse("PT" + rs.getString("duration") + "M"))
                .mpa(MPA.builder()
                       .id(rs.getInt("mpa_id"))
                        .rating(rs.getString("rating")).build())
                .build();
    }
}
