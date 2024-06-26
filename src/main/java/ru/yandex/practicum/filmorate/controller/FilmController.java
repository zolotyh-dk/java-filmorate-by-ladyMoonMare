package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer,Film> films = new HashMap<>();
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate FIRST_CINEMA_DATE = LocalDate.parse("28.12.1895",
            DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    private IdGenerator idGen = new IdGenerator();

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("addFilm attempt {}",film);


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
        validateFilm(film);
        film.setId(idGen.getId());
        idGen.reloadId();
        films.put(film.getId(),film);
        log.info("addFilm {} success",film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("updateFilm attempt {}",film);
        if (!films.containsKey(film.getId())) {
            log.warn("updateFilm failure - film {} has not been uploaded", film);
            throw new InvalidDataException("Film has not been uploaded");
        }
        validateFilm(film);
        films.put(film.getId(),film);
        log.info("updateFilm {} success",film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getName().isBlank() || film.getName().isEmpty()) {
            log.warn("Data error - invalid title {}",film.getName());
            throw new InvalidDataException("Invalid name");
        } else if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("Data error - invalid description length {}",film.getDescription().length());
            throw new InvalidDataException("Invalid description length");
        } else if (film.getReleaseDate().isBefore(FIRST_CINEMA_DATE) ||
                film.getReleaseDate().isAfter(LocalDate.now())) {
            log.warn("Data error - invalid release date {}",film.getReleaseDate());
            throw new InvalidDataException("Invalid date");
        } else if (film.getDuration().toMinutes() <= 0) {
            throw new InvalidDataException("Invalid duration");
        }
    }
}
