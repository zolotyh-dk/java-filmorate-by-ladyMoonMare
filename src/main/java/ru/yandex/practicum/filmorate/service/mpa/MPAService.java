package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.BaseService;
import ru.yandex.practicum.filmorate.storage.mpa.MPAStorage;


import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MPAService implements BaseService<MPA> {
    private final MPAStorage ms;

    @Override
    public List<MPA> getAll() {
        return ms.getAllRatings();
    }

    @Override
    public MPA getById(Integer id) {
        return ms.findRatingById(id).orElseThrow(
                () -> {
                    log.warn("Rating with id {} not found",id);
                    return new DataNotFoundException("Rating with id {} not found");
                }
        );
    }
}
