package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MPAStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage ls;
    private final MPAStorage ms;
    private final GenreStorage gs;

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        for (Film film : films) {
            film.setGenres(new HashSet<>(gs.getGenresByFilmId(film.getId())));
        }
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        Set<Genre> genres = new LinkedHashSet<>();
        film.setMpa(ms.findRatingById(film.getMpa().getId()).orElseThrow(() -> {
            log.warn("MPA with id {} not found",film.getMpa().getId());
            return new DataNotFoundException("MPA with id {} not found");
        }));
        Film f =  filmStorage.addFilm(film);
        for (Genre genre : f.getGenres()) {
            genres.add(gs.findGenreById(genre.getId()).orElseThrow(
                    () -> {
                        log.warn("Genre with id {} not found",genre.getId());
                        return new DataNotFoundException("Genre with id {} not found");
                    })
            );
            gs.addFilmGenre(f.getId(), genre.getId());
        }
        f.setGenres(genres);
        return f;
    }

    @Override
    public Film updateFilm(Film film) {
        Film f = getFilmById(film.getId());
        film.setMpa(ms.findRatingById(film.getMpa().getId()).orElseThrow(() -> {
            log.warn("MPA with id {} not found",film.getMpa().getId());
            return new DataNotFoundException("MPA with id {} not found");
        }));

        gs.removeFilmGenre(film.getId());
        Set<Genre> updateGenres = new HashSet<>();
        for (Genre genre : film.getGenres()) {
            updateGenres.add(gs.findGenreById(genre.getId()).orElseThrow(
                    () -> {
                        log.warn("Genre with id {} not found",genre.getId());
                        return new DataNotFoundException("Genre with id {} not found");
                    })
            );
            gs.addFilmGenre(film.getId(), genre.getId());
        }
        film.setGenres(updateGenres);
        return filmStorage.updateFilm(film);
    }

    @Override
    public Film getFilmById(Integer id) {
        return filmStorage.findFilmById(id).orElseThrow(
                () -> {
                    log.warn("Film with id {} not found",id);
                    return new DataNotFoundException("Film with id {} not found");
                }
        );
    }

    @Override
    public void addLike(Integer id, Integer userId) {
        ls.addLike(id, userId);
        log.info("user {} successfully liked film {}", userId, id);
    }

    @Override
    public void removeLike(Integer id, Integer userId) {
        ls.removeLike(id, userId);
        log.info("user {} successfully removed like from film {}", userId, id);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        List<Film> allFilms = filmStorage.getAllFilms();
        return allFilms.stream()
                .sorted(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        return ls.getLikesFromDb(o2.getId()).size() -
                                ls.getLikesFromDb(o1.getId()).size();
                    }
                })
                .limit(count)
                .toList();
    }
}
