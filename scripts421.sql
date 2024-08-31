ALTER TABLE student
    ADD CONSTRAINT age_constraint CHECK (age >= 16); -- Ограничение на возраст студента (от 16 лет)

ALTER TABLE student
	ADD CONSTRAINT name_unique UNIQUE (name),
	ALTER COLUMN name SET NOT NULL; -- Имена студентов должны быть уникальными и не равны нулю

ALTER TABLE faculty
	ADD CONSTRAINT name_color_unique UNIQUE (name, color); -- Пара “значение названия” - “цвет факультета” должна быть уникальной

ALTER TABLE student
	ALTER COLUMN age SET DEFAULT 20; -- При создании студента без возраста ему автоматически должно присваиваться 20 лет.

-- Чтобы не забыть как убираются ограничения, где age_constraint - удаляемое ограничение
-- ALTER TABLE student DROP CONSTRAINT age_constraint;
