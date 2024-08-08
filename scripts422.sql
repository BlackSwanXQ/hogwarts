CREATE DATABASE person_auto;

CREATE TABLE cars
(
    id    int8 PRIMARY KEY,
    model text,
    cost  int4
);

INSERT INTO cars (id, model, cost)
VALUES (1, 'car1', 10);
INSERT INTO cars (id, model, cost)
VALUES (2, 'car2', 20);
INSERT INTO cars (id, model, cost)
VALUES (3, 'car3', 30);
INSERT INTO cars (id, model, cost)
VALUES (4, 'car4', 40);


CREATE TABLE persons
(
    id           int8 PRIMARY KEY,
    name         text,
    age          int4,
    driveLicense boolean,
    car_id       int4 REFERENCES cars (id)
);

INSERT INTO persons (id, name, age, driveLicense, car_id)
VALUES (1, 'name1', 18, true, 1);
INSERT INTO persons (id, name, age, driveLicense, car_id)
VALUES (2, 'name2', 20, true, 2);
INSERT INTO persons (id, name, age, driveLicense)
VALUES (3, 'name3', 14, false);
INSERT INTO persons (id, name, age, driveLicense)
VALUES (4, 'name4', 20, false);
INSERT INTO persons (id, name, age, driveLicense, car_id)
VALUES (5, 'name5', 25, true, 2);
INSERT INTO persons (id, name, age, driveLicense)
VALUES (6, 'name6', 15, false);
INSERT INTO persons (id, name, age, driveLicense, car_id)
VALUES (7, 'name7', 21, true, 3);
INSERT INTO persons (id, name, age, driveLicense, car_id)
VALUES (8, 'name7', 21, true, 4);
INSERT INTO persons (id, name, age, driveLicense, car_id)
VALUES (9, 'name8', 27, true, 4);


CREATE TABLE person_auto as
SELECT persons.name,
       persons.age,
       cars.model
FROM persons
         JOIN cars on persons.car_id = cars.id;


SELECT persons.name,
       persons.age,
       cars.model
FROM persons
       FULL  JOIN cars on persons.car_id = cars.id;