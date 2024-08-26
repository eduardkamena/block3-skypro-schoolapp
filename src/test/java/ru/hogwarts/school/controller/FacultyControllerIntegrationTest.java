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
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
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
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void clearDatabase() {
        facultyRepository.deleteAll();
    }

    @Test
    void shouldCreateFaculty() {
        // given
        Faculty faculty = new Faculty("Griffindor", "Green");

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
        restTemplate.put("/faculty/" + faculty.getId(), facultyForUpdate);
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.getForEntity("/faculty/" + faculty.getId(), Faculty.class);

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
                "http://localhost:" + port + "/faculty/" + faculty.getId(),
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
                "http://localhost:" + port + "/faculty/" + faculty.getId(),
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
    public void shouldFindFacultyByColorOrName() throws Exception {
        Faculty faculty = new Faculty("name", "color");
        faculty = facultyRepository.save(faculty);

        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/nameOrColor?name=" + faculty.getName(), String.class))
                .isNotNull();
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/nameOrColor?color=" + faculty.getColor(), String.class))
                .isNotNull();
    }

    @Test
    public void shouldFindColorFaculty() throws Exception {
        Faculty faculty = new Faculty("name", "color");
        faculty = facultyRepository.save(faculty);

        // when

        ResponseEntity <List<Faculty>> facultyResponseEntity = restTemplate.exchange(
                "/faculty?color=" + faculty.getColor(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
        });

        // then
        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        List<Faculty> actualFaculty = facultyResponseEntity.getBody();
        assertEquals(actualFaculty.size(), 1);
        assertEquals(actualFaculty.get(0).getId(), faculty.getId());
        assertEquals(actualFaculty.get(0).getName(), faculty.getName());
        assertEquals(actualFaculty.get(0).getColor(), faculty.getColor());
    }
}
