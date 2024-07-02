package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InvalidDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer,Film> films = new HashMap<>();
    private IdGenerator idGen = new IdGenerator();

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        films.values()
                .forEach(f -> {
                    if (f.getName().equals(film.getName()) &&
                            f.getReleaseDate().equals(film.getReleaseDate())) {
                        log.warn("addFilm attempt failure - user {} is already uploaded",film);
                        throw new InvalidDataException("Film is already uploaded");
                    }
                });
        if (films.containsValue(film)) {
            log.warn("addFilm attempt failure - user {} is already uploaded",film);
            throw new InvalidDataException("Film is already uploaded");
        }
        film.setId(idGen.getId());
        idGen.reloadId();
        films.put(film.getId(),film);
        log.info("addFilm {} success",film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("updateFilm failure - film {} has not been uploaded", film);
            throw new InvalidDataException("Film has not been uploaded");
        }
        films.put(film.getId(),film);
        log.info("updateFilm {} success",film);
        return film;
    }
}
