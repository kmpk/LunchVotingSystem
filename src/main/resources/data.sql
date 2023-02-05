INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT(NAME, ADDRESS)
VALUES ('First restaurant', 'First restaurant address'),
       ('Second restaurant', 'Second restaurant address'),
       ('Third restaurant', 'Third restaurant address');

INSERT INTO DISH(RESTAURANT_ID, MENU_DATE, NAME, COST)
VALUES (1, CURRENT_DATE, 'Restaurant 1 dish 1', 10000),
       (1, CURRENT_DATE, 'Restaurant 1 dish 2', 20000),
       (1, CURRENT_DATE, 'Restaurant 1 dish 3', 30000),
       (2, CURRENT_DATE, 'Restaurant 2 dish 1', 40000),
       (2, CURRENT_DATE, 'Restaurant 2 dish 2', 50000),
       (1, DATEADD('DAY', -1, CURRENT_DATE), 'Restaurant 1 yesterday dish 1', 70000),
       (1, DATEADD('DAY', -1, CURRENT_DATE), 'Restaurant 1 yesterday dish 2', 80000);

INSERT INTO VOTE(VOTE_DATE, RESTAURANT_ID, USER_ID)
VALUES (CURRENT_DATE, 1, 1),
       (CURRENT_DATE, 2, 2),
       (DATEADD('DAY', -1, CURRENT_DATE), 2, 1);


