package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private static final LocalDate FIRST_CINEMA_DATE = LocalDate.parse("28.12.1895",
            DateTimeFormatter.ofPattern("dd.MM.yyyy"));


    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("addFilm attempt {}",film);
        validateFilm(film);
        filmService.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("updateFilm attempt {}",film);
        validateFilm(film);
        filmService.updateFilm(film);
        return film;
    }

    @Validated
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable @Positive Integer id) {
        log.info("attempt to get film by id {}",id);
        return filmService.getFilmById(id);
    }

    @Validated
    @PutMapping("/{id}/like/{userId}")
    public List<User> addLike(@PathVariable @Positive Integer id,
                              @PathVariable @Positive Integer userId) {
        log.info("attempt set like to film {} by user {}", id, userId);
        return filmService.addLike(id,userId);
    }

    @Validated
    @DeleteMapping("/{id}/like/{userId}")
    public List<User> removeLike(@PathVariable @Positive Integer id,
                                 @PathVariable @Positive Integer userId) {
        log.info("attempt remove like from film {} by user {}", id, userId);
        return filmService.removeLike(id,userId);
    }

    @Validated
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") @Positive Integer count) {
        return filmService.getPopularFilms(count);
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(FIRST_CINEMA_DATE) ||
                film.getReleaseDate().isAfter(LocalDate.now())) {
            log.warn("Data error - invalid release date {}",film.getReleaseDate());
            throw new ValidationException("Invalid date");
        } else if (film.getDuration().toMinutes() <= 0) {
            throw new ValidationException("Invalid duration");
        }
    }
}
