package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final FriendsStorage fs;

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
        isUserNotValid(user.getId());
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
    public  List<User> getUserFriends(Integer id) {
        isUserNotValid(id);
        return fs.getFriendsFromDb(id);
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        isUserNotValid(id);
        isUserNotValid(friendId);
        fs.addFriend(id,friendId);
        log.info("user {} successfully added to friend list", friendId);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        isUserNotValid(id);
        isUserNotValid(friendId);
        fs.deleteFriend(id,friendId);
        log.info("user {} successfully deleted from friend list", friendId);
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        List<User> commonFriends = new ArrayList<>();
        isUserNotValid(id);
        isUserNotValid(otherId);

        for (User u : fs.getFriendsFromDb(id)) {
            for (User ou : fs.getFriendsFromDb(otherId)) {
                if (u.getId() == ou.getId()) {
                    commonFriends.add(u);
                }
            }
        }
        return commonFriends;
    }

    public void isUserNotValid(Integer id) {
        if (userStorage.findUserById(id).isEmpty()) {
            log.warn("User with id {} not found",id);
            throw  new DataNotFoundException("User with id {} not found");
        }
    }
}
