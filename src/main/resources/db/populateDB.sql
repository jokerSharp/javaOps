DELETE FROM user_role;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2024-05-30 23:12', 'meal1', 470, 100000),
       ('2024-05-29 06:05', 'meal2', 570, 100000),
       ('2024-05-28 15:16', 'meal3', 300, 100000),
       ('2024-02-16 18:24', 'meal4', 274, 100000),
       ('2024-01-06 20:44', 'meal5', 936, 100000),
       ('2023-10-23 20:23', 'meal6', 670, 100001),
       ('2024-06-05 18:13', 'meal7', 825, 100001),
       ('2024-09-15 20:11', 'meal8', 709, 100001),
       ('2024-04-15 16:54', 'meal9', 216, 100001),
       ('2024-02-10 14:49', 'meal10', 747, 100001);