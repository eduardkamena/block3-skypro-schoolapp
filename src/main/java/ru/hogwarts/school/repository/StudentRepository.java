package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByAge(int age);

    List<Student> findByAgeBetween(int ageMin, int ageMax);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Integer getAllStudents();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Float getAverageStudentAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getLastPostedStudents();
}
