SELECT * FROM student;

SELECT * FROM faculty;

SELECT * FROM student WHERE age >= 10 AND age <= 20;

SELECT * FROM student WHERE age BETWEEN 10 AND 20;

SELECT student.name FROM student;

SELECT * FROM student WHERE NAME ILIKE '%e%'; -- ILIKE - без учета региста;

SELECT * FROM student WHERE student.age < student.id;

SELECT * FROM student ORDER BY age;

SELECT * FROM avatar;