DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2020-01-01 10:00:00', 'Завтрак', 1000, 100000),
       ('2020-01-01 12:00:00', 'Обед', 2000, 100000),
       ('2020-01-01 18:00:00', 'Ужин', 3000, 100000),
       ('2020-01-02 0:00:00', 'Еда на граничное значение', 3000, 100000),
       ('2020-01-02 10:00:00', 'Завтрак', 1000, 100001),
       ('2020-01-02 12:00:00', 'Обед', 2000, 100001),
       ('2020-01-02 20:00:00', 'Ужин', 3000, 100001);