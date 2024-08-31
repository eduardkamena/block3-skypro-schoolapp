package ru.hogwarts.school.service.Impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

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

        Optional<Student> studentFromDB = studentRepository.findById(id);
        if (studentFromDB.isPresent()) {
            Student temp = studentFromDB.get();
            temp.setName(student.getName());
            temp.setAge(student.getAge());
            studentRepository.save(temp);
        }
        return null;
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

    @Override
    public List<Student> findByAgeBetween(int ageMin, int ageMax) {
        return studentRepository.findByAgeBetween(ageMin, ageMax);
    }

    @Override
    public Faculty getFaculty(Long studentId) {
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElse(null);
    }

    @Override
    public Integer getAllStudents() {
        return studentRepository.getAllStudents();
    }

    @Override
    public Float getAverageStudentAge() {
        return studentRepository.getAverageStudentAge();
    }

    @Override
    public List<Student> getLastPostedStudents() {
        return studentRepository.getLastPostedStudents();
    }

}
