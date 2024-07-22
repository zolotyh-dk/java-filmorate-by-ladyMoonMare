package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.filmorate.util.deserializer.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.util.serializer.LocalDateSerializer;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NotNull
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @NotNull
    private int id;
    @Email
    @NotBlank
    @NotEmpty
    private String email;
    @NotBlank
    @NotEmpty
    private String login;
    private String name = "";
    @NotNull
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthday;

    public User(String email, String login, LocalDate birthday) {
        name = login;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
