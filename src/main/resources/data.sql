INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT(ID, NAME, ADDRESS)
VALUES (100, 'First restaurant', 'First restaurant address'),
       (101, 'Second restaurant', 'Second restaurant address'),
       (102, 'Third restaurant', 'Third restaurant address');


