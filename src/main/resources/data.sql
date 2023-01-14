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

INSERT INTO DISH(ID, RESTAURANT_ID, MENU_DATE, NAME, COST)
VALUES (1000, 100, CURRENT_DATE, 'Restaurant 1 dish 1', 10000),
       (1001, 100, CURRENT_DATE, 'Restaurant 1 dish 2', 20000),
       (1002, 100, CURRENT_DATE, 'Restaurant 1 dish 3', 30000),
       (1003, 101, CURRENT_DATE, 'Restaurant 2 dish 1', 40000),
       (1004, 101, CURRENT_DATE, 'Restaurant 2 dish 2', 50000),
       (1005, 100, DATEADD('DAY', -1, CURRENT_DATE), 'Restaurant 1 yesterday dish 1', 70000),
       (1006, 100, DATEADD('DAY', -1, CURRENT_DATE), 'Restaurant 1 yesterday dish 2', 80000);

INSERT INTO VOTE(ID, DATE, RESTAURANT_ID, USER_ID)
VALUES (10000, CURRENT_DATE, 100, 1),
       (10001, CURRENT_DATE, 101, 2),
       (10002, DATEADD('DAY', -1, CURRENT_DATE), 101, 1);


