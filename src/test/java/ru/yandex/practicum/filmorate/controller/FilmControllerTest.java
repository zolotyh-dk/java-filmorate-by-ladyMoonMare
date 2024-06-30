package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.InvalidDataException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    @Test
    public void shouldBePositiveIfFilmIsAdded() {
        FilmController fc = new FilmController();
        Film film = new Film("Film","", LocalDate.parse("2002-07-30"),
                Duration.parse("PT40M"));
        fc.addFilm(film);
        assertFalse(fc.getAllFilms().isEmpty());
        assertEquals(film,fc.getAllFilms().get(0));
    }

    @Test
    public void shouldBePositiveIfFilmIsUpdated() {
        FilmController fc = new FilmController();
        Film film = new Film("Film","", LocalDate.parse("2002-07-30"),
                Duration.parse("PT40M"));
        fc.addFilm(film);
        film.setDescription("description");
        fc.updateFilm(film);
        assertEquals("description",fc.getAllFilms().get(0).getDescription());
    }

    @Test
    public void shouldThrowExcForInvalidName() {
        FilmController fc = new FilmController();
        Film film = new Film("","", LocalDate.parse("2002-07-30"),
                Duration.parse("PT40M"));
        assertThrows(InvalidDataException.class, () -> fc.addFilm(film));

        Film film1 = new Film(" ","", LocalDate.parse("2002-07-30"),
                Duration.parse("PT40M"));
        assertThrows(InvalidDataException.class, () -> fc.addFilm(film1));
    }

    @Test
    public void shouldThrowExcForInvalidDescription() {
        FilmController fc = new FilmController();
        Film film = new Film("Title","1".repeat(201), LocalDate.parse("2002-07-30"),
                Duration.parse("PT40M"));
        assertThrows(InvalidDataException.class, () -> fc.addFilm(film));
    }

    @Test
    public void shouldThrowExcForInvalidReleaseDate() {
        FilmController fc = new FilmController();
        Film film = new Film("Title","", LocalDate.parse("1700-01-01"),
                Duration.parse("PT40M"));
        assertThrows(InvalidDataException.class, () -> fc.addFilm(film));
    }

    @Test
    public void shouldThrowExcForInvalidDuration() {
        FilmController fc = new FilmController();
        Film film = new Film("Title","", LocalDate.parse("2002-07-30"),
                Duration.parse("PT-400M"));
        assertThrows(InvalidDataException.class, () -> fc.addFilm(film));

        Film film1 = new Film("Title1","", LocalDate.parse("2002-07-30"),
                Duration.parse("PT0M"));
        assertThrows(InvalidDataException.class, () -> fc.addFilm(film1));
    }
}
