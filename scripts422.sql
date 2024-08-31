-- Описание структуры: у каждого человека есть машина.
-- Причем несколько человек могут пользоваться одной машиной.
-- У каждого человека есть имя, возраст и признак того, что у него есть права (или их нет).
-- У каждой машины есть марка, модель и стоимость.
-- Также не забудьте добавить таблицам первичные ключи и связать их.


-- Создаем таблицу людей (водителей)
CREATE TABLE people (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    age INTEGER,
    driver_license BOOLEAN
);

-- Создаем таблицу машин
CREATE TABLE vehicle (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(255),
    model VARCHAR(255),
    value_price MONEY
);

-- Устанавливаем связь по PrimaryKey vehicle
ALTER TABLE people ADD vehicle_id SERIAL REFERENCES vehicle (id);

-- Добавляем машины
INSERT INTO vehicle (brand, model, value_price) VALUES ('Mercedes', 'G', 15_000_000);
INSERT INTO vehicle (brand, model, value_price) VALUES ('AUDI', 'RS6', 12_000_000);
INSERT INTO vehicle (brand, model, value_price) VALUES ('Maserati', 'Grecale', 10_000_000);

-- Добавляем людей (водителей)
INSERT INTO people (name, age, driver_license, vehicle_id) VALUES ('Bob', 20, 'on', 1);
INSERT INTO people (name, age, driver_license, vehicle_id) VALUES ('Kate', 19, 'on', 3);
INSERT INTO people (name, age, driver_license, vehicle_id) VALUES ('ED', 37, 'on', 2);
INSERT INTO people (name, age, driver_license, vehicle_id) VALUES ('Karry', 37, 'on', 1);

-- Формируем JOIN запрос - сводная таблица с учетом требований нормализации (НФ)
SELECT people.name, people.age, people.driver_license,
		vehicle.brand, vehicle.model, value_price
FROM people
INNER JOIN vehicle ON people.vehicle_id = vehicle.id;
