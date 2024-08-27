package ru.hogwarts.school.controller;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyService facultyService;

    @Test
    public void shouldCreateFaculty() throws Exception {
        // given
        Long id = 1L;
        String name = "name";
        String color = "color";

        // when
        JSONObject userObject = new JSONObject();
        userObject.put("name", name);
        userObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);
        // when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty") //send
                        .content(userObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void shouldReadFaculty() throws Exception {
        // given
        Long id = 1L;
        String name = "name";
        String color = "color";

        Faculty faculty = new Faculty(id, name, color);

        // when
        when(facultyService.readFaculty(id)).thenReturn(faculty);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateFaculty() throws Exception {
        // given
        Long id = 1L;
        String name = "name";
        String newName = "newName";
        String color = "color";
        String newColor = "newColor";

        // when
        Faculty faculty = new Faculty(id, name, color);
        Faculty updatedFaculty = new Faculty(id, newName, newColor);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", newName);
        jsonObject.put("color", newColor);

        when(facultyService.updateFaculty(eq(id), any(Faculty.class))).thenReturn(updatedFaculty);
        // when(studentService.readStudent(id)).thenReturn(updatedStudent);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + id)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value(newColor));
    }

    @Test
    void shouldUpdateFacultyNegativeTest() throws Exception {
        // given
        Long id = 1L;
        String name = "name";
        String newName = "newName";
        String color = "color";
        String newColor = "newColor";

        // when
        Faculty faculty = new Faculty(id, name, color);
        Faculty updatedFaculty = new Faculty(id, newName, newColor);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", newName);
        jsonObject.put("color", newColor);

        when(facultyService.readFaculty(id)).thenReturn(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/update/" + id)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteFaculty() throws Exception {
        // given
        Long id = 1L;
        String name = "name";
        String color = "color";

        Faculty faculty = new Faculty(id, name, color);

        // when
        when(facultyService.deleteFaculty(id)).thenReturn(faculty);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldFindColorFaculty() throws Exception {
        // given
        List<Faculty> faculties = new ArrayList<>();
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("name");
        faculty.setColor("color");
        faculties.add(faculty);

        // when
        when(facultyService.findColorFaculty(faculty.getColor())).thenReturn(faculties);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color=" + faculty.getColor()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].color", Matchers.equalTo("color")));
    }

    @Test
    public void shouldFindByAgeBetween() throws Exception {
        // given
        List<Faculty> faculties = new ArrayList<>();

        Faculty faculty = new Faculty();
        faculty.setName("name");
        faculty.setColor("color");
        faculties.add(faculty);

        String name = "name";
        String color = "color";

        // when
        when(facultyService.findByNameIgnoreCaseOrColorIgnoreCase(name, color)).thenReturn(faculties);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/nameOrColor?name=" + name + "&color=" + color))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("name")))
                .andExpect(jsonPath("$[0].color", Matchers.equalTo("color")));
    }

}
