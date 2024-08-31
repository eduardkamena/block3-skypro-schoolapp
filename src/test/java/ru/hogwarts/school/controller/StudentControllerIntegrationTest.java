package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class StudentControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void clearDatabase() {
        studentRepository.deleteAll();
    }

    @Test
    void shouldCreateStudent() {
        // given
        Student student = new Student("name", 20);

        // when
        ResponseEntity<Student> studentResponseEntity = restTemplate.postForEntity(
                "/student",
                student,
                Student.class
        );

        // then
        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Student actualStudent = studentResponseEntity.getBody();
        assert actualStudent != null;
        assertNotNull(actualStudent.getId());
        assertEquals(actualStudent.getName(), student.getName());
        assertThat(actualStudent.getAge()).isEqualTo(student.getAge());
    }

    @Test
    void shouldUpdateStudent() {
        // given
        Student student = new Student("name", 20);
        student = studentRepository.save(student);

        Student studentForUpdate = new Student("newName", 25);

        // when
        restTemplate.put(
                "/student/" + student.getId(),
                studentForUpdate
        );

        ResponseEntity<Student> studentResponseEntity = restTemplate.getForEntity(
                "/student/" + student.getId(),
                Student.class
        );

        // then
        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Student actualStudent = studentResponseEntity.getBody();
        assert actualStudent != null;
        assertEquals(actualStudent.getId(), student.getId());
        assertEquals(actualStudent.getName(), studentForUpdate.getName());
        assertEquals(actualStudent.getAge(), studentForUpdate.getAge());
    }

    @Test
    void shouldReadStudent() {
        // given
        Student student = new Student("name", 20);
        student = studentRepository.save(student);

        // when
        ResponseEntity<Student> studentResponseEntity = restTemplate.getForEntity(
                "/student/" + student.getId(),
                Student.class
        );

        // then
        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Student actualFaculty = studentResponseEntity.getBody();
        assert actualFaculty != null;
        assertEquals(actualFaculty.getId(), student.getId());
        assertEquals(actualFaculty.getName(), student.getName());
        assertEquals(actualFaculty.getAge(), student.getAge());
    }

    @Test
    void shouldDeleteStudent() {
        // given
        Student student = new Student("name", 20);
        student = studentRepository.save(student);

        // when
        ResponseEntity<Student> studentResponseEntity = restTemplate.exchange(
                "/student/" + student.getId(),
                HttpMethod.DELETE,
                null,
                Student.class
        );

        // then
        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        assertThat(studentRepository.findById(student.getId())).isNotPresent();
    }

    @Test
    public void shouldFindAgeStudent() throws Exception {
        // given
        Student student = new Student("name", 20);
        student = studentRepository.save(student);

        // when
        ResponseEntity<List<Student>> studentResponseEntity = restTemplate.exchange(
                "/student?age=" + student.getAge(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        List<Student> actualStudent = studentResponseEntity.getBody();
        assert actualStudent != null;
        assertEquals(actualStudent.size(), 1);
        assertEquals(actualStudent.get(0).getId(), student.getId());
        assertEquals(actualStudent.get(0).getName(), student.getName());
        assertEquals(actualStudent.get(0).getAge(), student.getAge());
    }

    @Test
    public void shouldFindByAgeBetweenStudent() throws Exception {
        // given
        Student firstStudent = new Student("firstName", 20);
        firstStudent = studentRepository.save(firstStudent);

        Student secondStudent = new Student("secondName", 40);
        secondStudent = studentRepository.save(secondStudent);

        int ageMin = 10;
        int ageMax = 30;

        // when
        ResponseEntity<List<Student>> studentResponseEntity = restTemplate.exchange(
                "/student/ageBetween" + "?ageMin=" + ageMin + "&ageMax=" + ageMax,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        List<Student> actualStudent = studentResponseEntity.getBody();
        assert actualStudent != null;
        assertEquals(actualStudent.size(), 1);
    }

    @Test
    public void shouldFindFacultyByStudent() throws Exception {
        // given
        Faculty faculty = new Faculty("name", "color");
        faculty = facultyRepository.save(faculty);

        Student student = new Student("name", 20);
        student.setFaculty(faculty);
        student = studentRepository.save(student);

        // when
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                "/student/" + student.getId() + "/faculty",
                HttpMethod.GET,
                null,
                Faculty.class
        );

        // then
        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        assertThat(facultyRepository.findById(faculty.getId())).isPresent();
    }

}
