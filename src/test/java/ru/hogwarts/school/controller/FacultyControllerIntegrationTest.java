package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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
public class FacultyControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void clearDatabase() {
        facultyRepository.deleteAll();
    }

    @Test
    void shouldCreateFaculty() {
        // given
        Faculty faculty = new Faculty("name", "color");

        // when
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty",
                faculty,
                Faculty.class
        );

        // then
        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertNotNull(actualFaculty.getId());
        assertEquals(actualFaculty.getName(), faculty.getName());
        assertThat(actualFaculty.getColor()).isEqualTo(faculty.getColor());
    }

    @Test
    void shouldUpdateFaculty() {
        // given
        Faculty faculty = new Faculty("name", "color");
        faculty = facultyRepository.save(faculty);

        Faculty facultyForUpdate = new Faculty("newName", "newColor");

        // when
        restTemplate.put(
                "/faculty/" + faculty.getId(),
                facultyForUpdate
        );

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.getForEntity(
                "/faculty/" + faculty.getId(),
                Faculty.class
        );

        // then
        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertEquals(actualFaculty.getId(), faculty.getId());
        assertEquals(actualFaculty.getName(), facultyForUpdate.getName());
        assertEquals(actualFaculty.getColor(), facultyForUpdate.getColor());
    }

    @Test
    void shouldReadFaculty() {
        // given
        Faculty faculty = new Faculty("name", "color");
        faculty = facultyRepository.save(faculty);

        // when
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.getForEntity(
                "/faculty/" + faculty.getId(),
                Faculty.class
        );

        // then
        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertEquals(actualFaculty.getId(), faculty.getId());
        assertEquals(actualFaculty.getName(), faculty.getName());
        assertEquals(actualFaculty.getColor(), faculty.getColor());
    }

    @Test
    void shouldDeleteFaculty() {
        // given
        Faculty faculty = new Faculty("name", "color");
        faculty = facultyRepository.save(faculty);

        // when
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                "/faculty/" + faculty.getId(),
                HttpMethod.DELETE,
                null,
                Faculty.class
        );

        // then
        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        assertThat(facultyRepository.findById(faculty.getId())).isNotPresent();
    }

    @Test
    public void shouldFindColorFaculty() throws Exception {
        // given
        Faculty faculty = new Faculty("name", "color");
        faculty = facultyRepository.save(faculty);

        // when
        ResponseEntity<List<Faculty>> facultyResponseEntity = restTemplate.exchange(
                "/faculty?color=" + faculty.getColor(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        List<Faculty> actualFaculty = facultyResponseEntity.getBody();
        assertEquals(actualFaculty.size(), 1);
        assertEquals(actualFaculty.get(0).getId(), faculty.getId());
        assertEquals(actualFaculty.get(0).getName(), faculty.getName());
        assertEquals(actualFaculty.get(0).getColor(), faculty.getColor());
    }

    @Test
    public void shouldFindFacultyByColorOrName() throws Exception {
        // given
        Faculty faculty = new Faculty("name", "color");
        faculty = facultyRepository.save(faculty);

        // when
        ResponseEntity<List<Faculty>> facultyResponseEntityColor = restTemplate.exchange(
                "/faculty/nameOrColor?color=" + faculty.getColor(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        ResponseEntity<List<Faculty>> facultyResponseEntityName = restTemplate.exchange(
                "/faculty/nameOrColor?name=" + faculty.getName(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertNotNull(facultyResponseEntityColor);
        assertEquals(facultyResponseEntityColor.getStatusCode(), HttpStatusCode.valueOf(200));

        assertNotNull(facultyResponseEntityName);
        assertEquals(facultyResponseEntityName.getStatusCode(), HttpStatusCode.valueOf(200));

        List<Faculty> actualFacultyColor = facultyResponseEntityColor.getBody();
        assertEquals(actualFacultyColor.size(), 1);
        assertEquals(actualFacultyColor.get(0).getId(), faculty.getId());
        assertEquals(actualFacultyColor.get(0).getName(), faculty.getName());
        assertEquals(actualFacultyColor.get(0).getColor(), faculty.getColor());

        List<Faculty> actualFacultyName = facultyResponseEntityName.getBody();
        assertEquals(actualFacultyName.size(), 1);
        assertEquals(actualFacultyName.get(0).getId(), faculty.getId());
        assertEquals(actualFacultyName.get(0).getName(), faculty.getName());
        assertEquals(actualFacultyName.get(0).getColor(), faculty.getColor());
    }

    @Test
    public void shouldFindStudentsByFaculty() throws Exception {
        // given
        Faculty faculty = new Faculty("name", "color");
        faculty = facultyRepository.save(faculty);

        Student student = new Student("name", 20);
        student = studentRepository.save(student);
        student.setFaculty(faculty);

        // when
        ResponseEntity<List<Student>> studentResponseEntity = restTemplate.exchange(
                "/faculty/" + faculty.getId() + "/student",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        assertThat(studentRepository.findById(student.getId())).isPresent();
    }

    @Test
    public void shouldGetLongestFacultyName() throws Exception {
        // given
        Faculty facultyFirst = new Faculty("short", "color"); // 5 letters
        facultyFirst = facultyRepository.save(facultyFirst);

        Faculty facultySecond = new Faculty("longest", "color"); // 7 letters
        facultySecond = facultyRepository.save(facultySecond);

        // when
        String facultyResponseEntity = restTemplate.getForObject(
                "/faculty/" + "longest_faculty_name",
                String.class
        );

        // then
        assertThat(facultyResponseEntity).isEqualTo("longest");
    }

}
