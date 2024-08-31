-- JOIN-запрос для получения информации обо всех студентах (только имя и возраст)
-- школы Хогвартс вместе с названиями факультетов и цвета
SELECT student.name, student.age,
		faculty.name, faculty.color
FROM student
FULL JOIN faculty ON student.faculty_id = faculty.id;

-- Составить второй JOIN-запрос, чтобы получить только тех студентов, у которых есть аватарки
SELECT student.name, student.age,
		avatar.file_path, avatar.media_type
FROM student
INNER JOIN avatar ON avatar.student_id = student.id;
