TRUNCATE TABLE user_role CASCADE;
TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE meals CASCADE;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2024-10-01 10:00:00', 'Завтрак', 400, 100000),
       ('2024-10-01 12:00:00', 'Обед', 800, 100000),
       ('2024-10-01 18:00:00', 'Ужин', 500, 100000),
       ('2024-10-02 0:00:00', 'Еда на граничное значение', 3000, 100000),
       ('2024-10-02 10:00:00', 'Завтрак', 1000, 100000),
       ('2024-10-02 12:00:00', 'Обед', 2000, 100000),
       ('2024-10-02 18:00:00', 'Ужин', 3000, 100000),
       ('2024-10-01 10:00:00', 'Завтрак', 400, 100001),
       ('2024-10-01 12:00:00', 'Обед', 800, 100001),
       ('2024-10-01 20:00:00', 'Ужин', 500, 100001),
       ('2024-10-02 0:00:00', 'Еда на граничное значение', 3000, 100001),
       ('2024-10-02 10:00:00', 'Завтрак', 1000, 100001),
       ('2024-10-02 12:00:00', 'Обед', 2000, 100001),
       ('2024-10-02 18:00:00', 'Ужин', 3000, 100001);