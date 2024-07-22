package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage gs;

    public List<Genre> getAllGenres() {
        return gs.getAllGenres();
    }

    public Genre getGenreById(Integer id) {
        return gs.findGenreById(id).orElseThrow(
                () -> {
                    log.warn("Genre with id {} not found",id);
                    return new DataNotFoundException("Genre with id {} not found");
                }
        );
    }
}
