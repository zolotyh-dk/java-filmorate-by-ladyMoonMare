package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MPAStorage;

import java.util.Comparator;
import java.util.List;

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
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        film.setMpa(ms.findRatingById(film.getMpa().getId()).orElseThrow(() -> {
            log.warn("MPA with id {} not found",film.getMpa().getId());
            return new DataNotFoundException("MPA with id {} not found");
        }));
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        Film f = getFilmById(film.getId());
        MPA mpa = ms.findRatingById(f.getMpa().getId()).orElseThrow(() -> {
            log.warn("MPA with id {} not found",film.getMpa().getId());
            return new DataNotFoundException("MPA with id {} not found");
        });
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
