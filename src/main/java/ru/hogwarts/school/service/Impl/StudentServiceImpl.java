package ru.hogwarts.school.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student readStudent(long id) {
        return studentRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        return studentRepository
                .findById(id)
                .map(studentFromDB -> {
                    studentFromDB.setName(student.getName());
                    studentFromDB.setAge(student.getAge());
                    studentRepository.save(studentFromDB);
                    return studentFromDB;
                })
                .orElse(null);
    }

    @Override
    public Student deleteStudent(long id) {
        return studentRepository
                .findById(id)
                .map(student -> {
                    studentRepository.deleteById(id);
                    return student;
                })
                .orElse(null);
    }

    @Override
    public List<Student> findAgeStudent(int age) {
        return studentRepository.findAllByAge(age);
    }

}
