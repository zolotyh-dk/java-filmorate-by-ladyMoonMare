package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        log.info("attempt to get film by id {}",id);
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public List<User> addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("attempt set like to film {} by user {}", id, userId);
        return filmService.addLike(id,userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public List<User> removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("attempt remove like from film {} by user {}", id, userId);
        return filmService.removeLike(id,userId);
    }

    @GetMapping("/popular?count={count}")
    public List<Film> getPopularFilms(@PathVariable Optional<Integer> count) {
        if (count.isEmpty()) {
            count = Optional.of(10);
        } else if (count.get() <= 0) {
            throw new ValidationException("Number of films must be positive");
        }
        return filmService.getPopularFilms(count.get());
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
