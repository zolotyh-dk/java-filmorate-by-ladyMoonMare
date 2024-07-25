CREATE TABLE IF NOT EXISTS app_users(
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
email VARCHAR NOT NULL,
login VARCHAR(60) NOT NULL,
name VARCHAR,
birthday DATE NOT NULL);

CREATE TABLE IF NOT EXISTS films(
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
title VARCHAR NOT NULL,
description VARCHAR(200),
releaseDate DATE NOT NULL,
duration INTEGER NOT NULL,
mpa_id INTEGER);

CREATE TABLE IF NOT EXISTS genres(
id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name VARCHAR);

CREATE TABLE IF NOT EXISTS mpa(
mpa_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
rating VARCHAR(10));

CREATE TABLE IF NOT EXISTS film_genre(
film_id INTEGER,
genre_id INTEGER);

CREATE TABLE IF NOT EXISTS likes(
film_id INTEGER,
user_id INTEGER);

CREATE TABLE IF NOT EXISTS friends(
user_id INTEGER,
friend_id INTEGER);

