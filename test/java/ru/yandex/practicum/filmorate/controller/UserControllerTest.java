package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.InvalidDataException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    @Test
    public void shouldThrowExcForInvalidLogin() {
        UserController uc = new UserController();
        User user = new User("ya@yandex.ru","", "name",
                LocalDate.parse("2002-07-30"));
        assertThrows(InvalidDataException.class,() -> uc.addUser(user));

        User user1 = new User("ya@yandex.ru"," ", "name",
                LocalDate.parse("2002-07-30"));
        assertThrows(InvalidDataException.class,() -> uc.addUser(user1));

        user.setLogin("login");
        uc.addUser(user);
        user1.setLogin("login");
        assertThrows(InvalidDataException.class,() -> uc.addUser(user1));
    }

    @Test
    public void shouldBePositiveIfUserIsAdded() {
        UserController uc = new UserController();
        User user = new User("ya@yandex.ru","user", "name",
                LocalDate.parse("2002-07-30"));
        uc.addUser(user);
        assertFalse(uc.getAllUsers().isEmpty());
        assertTrue(uc.getAllUsers().contains(user));
    }

    @Test
    public void shouldBePositiveIfUserIsUpdated() {
        UserController uc = new UserController();
        User user = new User("ya@yandex.ru","user", "name",
                LocalDate.parse("2002-07-30"));
        uc.addUser(user);
        user.setName("UploadedName");
        uc.updateUser(user);
        assertEquals("UploadedName",uc.getAllUsers().get(0).getName());
    }

    @Test
    public void shouldThrowExcForInvalidEmail() {
        UserController uc = new UserController();
        User user = new User("","login", "name",
                LocalDate.parse("2002-07-30"));
        assertThrows(InvalidDataException.class,() -> uc.addUser(user));

        User user1 = new User("yandex.ru","login1", "name",
                LocalDate.parse("2002-07-30"));
        assertThrows(InvalidDataException.class,() -> uc.addUser(user1));

        User user2 = new User(" ","login2", "name",
                LocalDate.parse("2002-07-30"));
        assertThrows(InvalidDataException.class,() -> uc.addUser(user2));
    }

    @Test
    public void shouldThrowExcForInvalidBirthday() {
        UserController uc = new UserController();
        User user = new User("ya@yandex.ru","login", "name",
                LocalDate.parse("2024-07-30"));
        assertThrows(InvalidDataException.class,() -> uc.addUser(user));

        User user1 = new User("ya@yandex.ru","login1", "name",
                LocalDate.parse("2024-06-26"));
        assertThrows(InvalidDataException.class,() -> uc.addUser(user1));
    }

    @Test
    public void shouldSetNameForLoginIfNameIsEmpty() {
        UserController uc = new UserController();
        User user = new User("ya@yandex.ru","login", "",
                LocalDate.parse("2002-07-30"));
        uc.addUser(user);
        assertEquals(user.getLogin(),user.getName());

        User user1 = new User("ya@yandex.ru","login1", " ",
                LocalDate.parse("2002-07-30"));
        uc.addUser(user1);
        assertEquals(user1.getLogin(),user1.getName());
    }
}
