package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentService {

    Student createStudent(Student student);

    Student readStudent(long id);

    Student updateStudent(Long id, Student student);

    Student deleteStudent(long id);

    List<Student> findAgeStudent(int age);

    List<Student> findByAgeBetween(int ageMin, int ageMax);

    Faculty getFaculty(Long studentId);

    Integer getAllStudents();

    Float getAverageStudentAge();

    List<Student> getLastPostedStudents();

}
