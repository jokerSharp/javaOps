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
VALUES ('2024-05-30 23:12', 'user meal1', 470, 100000),
       ('2024-05-29 06:05', 'user meal2', 570, 100000),
       ('2024-05-28 15:16', 'user meal3', 300, 100000),
       ('2024-02-16 18:24', 'user meal4', 1100, 100000),
       ('2024-02-16 16:44', 'user meal5', 1200, 100000),
       ('2024-10-23 20:23', 'admin meal1', 670, 100001),
       ('2024-10-22 18:13', 'admin meal2', 825, 100001),
       ('2024-10-21 20:11', 'admin meal3', 709, 100001),
       ('2024-04-15 16:54', 'admin meal4', 1500, 100001),
       ('2024-04-15 14:49', 'admin meal5', 900, 100001),
       ('2024-10-20 00:00', 'user meal6', 800, 100000);