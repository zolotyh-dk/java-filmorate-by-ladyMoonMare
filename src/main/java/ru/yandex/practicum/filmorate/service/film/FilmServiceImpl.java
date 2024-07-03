package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
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
    public List<User> addLike(Integer id, Integer userId) {
        User user = userService.getUserById(userId);
        Film film = getFilmById(id);
        film.getLikes().add(user);
        log.info("user {} successfully liked film {}", userId, id);
        return new ArrayList<>(film.getLikes());
    }

    @Override
    public List<User> removeLike(Integer id, Integer userId) {
        User user = userService.getUserById(userId);
        Film film = getFilmById(id);
        film.getLikes().remove(user);
        log.info("user {} successfully removed like from film {}", userId, id);
        return new ArrayList<>(film.getLikes());
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        List<Film> allFilms = filmStorage.getAllFilms();
        return allFilms.stream()
                .sorted(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        return o2.getLikes().size() - o1.getLikes().size();
                    }
                })
                .limit(count)
                .toList();
    }
}
