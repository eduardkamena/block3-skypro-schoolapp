package ru.hogwarts.school.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        logger.warn("Student age must be >16");
        return studentRepository.save(student);
    }

    @Override
    public Student readStudent(long id) {
        logger.info("Was invoked method for get student");
        return studentRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        logger.info("Was invoked method for update student");
        Optional<Student> studentFromDB = studentRepository.findById(id);
        if (studentFromDB.isPresent()) {
            Student temp = studentFromDB.get();
            temp.setName(student.getName());
            temp.setAge(student.getAge());
            studentRepository.save(temp);
        }
        logger.debug("Student by id {} should be updated", id);
        return null;
    }

    @Override
    public Student deleteStudent(long id) {
        logger.info("Was invoked method for delete student");
        logger.debug("Student by id {} should be deleted", id);
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
        logger.info("Was invoked method for find student by age");
        return studentRepository.findAllByAge(age);
    }

    @Override
    public List<Student> findByAgeBetween(int ageMin, int ageMax) {
        logger.info("Was invoked method for find student by age between");
        logger.warn("ageMin must be < ageMax");
        return studentRepository.findByAgeBetween(ageMin, ageMax);
    }

    @Override
    public Faculty getFaculty(Long studentId) {
        logger.info("Was invoked method for get student faculty by studentId {}", studentId);
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElse(null);
    }

    @Override
    public Integer getAllStudents() {
        logger.info("Was invoked method for get all students");
        return studentRepository.getAllStudents();
    }

    @Override
    public Float getAverageStudentAge() {
        logger.info("Was invoked method for get average student age");
        return studentRepository.getAverageStudentAge();
    }

    @Override
    public List<Student> getLastPostedStudents() {
        logger.info("Was invoked method for find last 5 posted students");
        return studentRepository.getLastPostedStudents();
    }

    @Override
    public List<String> getStudentsNamesStartsWith() {
        logger.info("Was invoked method for get students names starts with E");
        return studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(f -> f.startsWith("E")) // Сделал начало с Е, так как на А у меня нет студентов
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Double getAverageAgeAllStudentsStream() {
        logger.info("Was invoked method for get average age of students by stream impl");
        return studentRepository.findAll()
                .stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(0);
    }

    @Override
    public void printParallelThread() {

        List<Student> students = studentRepository.findAll();

        // делал через sout, но через logger нагляднее, так как показывается поток, который работает
        logger.info("[0] {}", students.get(0).getName());
        logger.info("[1] {}", students.get(1).getName());

        new Thread(() -> {
            logger.info("[2] {}", students.get(2).getName());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            logger.info("[3] {}", students.get(3).getName());
        }).start();


        new Thread(() -> {
            logger.info("[4] {}", students.get(4).getName());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            logger.info("[5] {}", students.get(5).getName());
        }).start();
    }

    @Override
    public void printSynchronizedThread() {

        List<Student> students = studentRepository.findAll();

        // залокал поток на List<Student> students, не стал создавать отдельный метод и отдельный объект
        // и снова через logger, так как видно, что потоки последовательны
        synchronized (students) {
            logger.info("[0] {}", students.get(0).getName());
            logger.info("[1] {}", students.get(1).getName());
        }
        new Thread(() -> {
            synchronized (students) {
                logger.info("[2] {}", students.get(2).getName());
                logger.info("[3] {}", students.get(3).getName());
            }
        }).start();

        new Thread(() -> {
            synchronized (students) {
                logger.info("[4] {}", students.get(4).getName());
                logger.info("[5] {}", students.get(5).getName());
            }
        }).start();
    }

}
