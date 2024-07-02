package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User addUser(User user) {
       userStorage.addUser(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        userStorage.updateUser(user);
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        return userStorage.findUserById(id).orElseThrow(
                () -> {
                    log.warn("User with id {} not found",id);
                    return new DataNotFoundException("User with id {} not found");
                }
        );
    }

    @Override
    public  List<String> getUserFriends(Integer id) {
        return new ArrayList<>(getUserById(id).getFriends());
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.getFriends().add(friend.getLogin());
        friend.getFriends().add(user.getLogin());
        log.info("user {} successfully added to friend list", friendId);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.getFriends().remove(friend.getLogin());
        friend.getFriends().remove(user.getLogin());
        log.info("user {} successfully deleted from friend list", friendId);
    }

    @Override
    public List<String> getCommonFriends(Integer id, Integer otherId) {
        User user = getUserById(id);
        User otherUser = getUserById(otherId);
        List<String> commonFriends = new ArrayList<>();
        for (String i : user.getFriends()) {
            for (String oi: otherUser.getFriends()) {
                if (Objects.equals(i, oi)) {
                    commonFriends.add(i);
                }
            }
        }
        return commonFriends;
    }
}
