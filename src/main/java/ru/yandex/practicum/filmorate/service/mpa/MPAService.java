package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MPADbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MPAService {
    private final MPADbStorage ms;

    public List<MPA> getAllRatings() {
        return ms.getAllRatings();
    }

    public MPA getRatingById(Integer id) {
        return ms.findRatingById(id).orElseThrow(
                () -> {
                    log.warn("Rating with id {} not found",id);
                    return new DataNotFoundException("Rating with id {} not found");
                }
        );
    }
}
