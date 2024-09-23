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
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentService studentService;

    @Test
    public void shouldCreateStudent() throws Exception {
        // given
        Long id = 1L;
        String name = "name";
        int age = 20;

        // when
        JSONObject userObject = new JSONObject();
        userObject.put("name", name);
        userObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentService.createStudent(any(Student.class))).thenReturn(student);
        // when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student") //send
                        .content(userObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    void shouldReadStudent() throws Exception {
        // given
        Long id = 1L;
        String name = "name";
        int age = 20;

        Student student = new Student(id, name, age);

        // when
        when(studentService.readStudent(id)).thenReturn(student);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + id))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateStudent() throws Exception {
        // given
        Long id = 1L;
        String name = "name";
        String newName = "newName";
        int age = 20;
        int newAge = 30;

        // when
        Student student = new Student(id, name, age);
        Student updatedStudent = new Student(id, newName, newAge);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", newName);
        jsonObject.put("age", newAge);

        when(studentService.updateStudent(eq(id), any(Student.class))).thenReturn(updatedStudent);
        // when(studentService.readStudent(id)).thenReturn(updatedStudent);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + id)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(newAge));
    }

    @Test
    void shouldUpdateStudentNegativeTest() throws Exception {
        // given
        Long id = 1L;
        String name = "name";
        String newName = "newName";
        int age = 20;
        int newAge = 30;

        // when
        Student student = new Student(id, name, age);
        Student updatedStudent = new Student(id, newName, newAge);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", newName);
        jsonObject.put("age", newAge);

        when(studentService.readStudent(id)).thenReturn(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/update/" + id)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteStudent() throws Exception {
        // given
        long id = 1L;
        String name = "name";
        int age = 20;
        Student student = new Student(id, name, age);


        // when
        when(studentService.deleteStudent(id)).thenReturn(student);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindAgeStudent() throws Exception {
        // given
        Long id = 1L;
        String name = "name";
        int age = 20;

        Student student = new Student(id, name, age);

        // when
        when(studentService.findAgeStudent(age)).thenReturn(new ArrayList<>(List.of(student)));

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?age=" + age))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldFindAgeStudentAlternativeTest() throws Exception {
        // given
        List<Student> students = new ArrayList<>();
        Student student = new Student();
        student.setId(1L);
        student.setName("name");
        student.setAge(20);
        students.add(student);

        // when
        when(studentService.findAgeStudent(student.getAge())).thenReturn(students);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?age=" + student.getAge()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("name")));
    }

    @Test
    public void shouldFindByAgeBetween() throws Exception {
        // given
        List<Student> students = new ArrayList<>();

        Student firstStudent = new Student();
        firstStudent.setName("firstName");
        firstStudent.setAge(20);
        students.add(firstStudent);

        Student secondStudent = new Student();
        secondStudent.setName("secondName");
        secondStudent.setAge(40);
        students.add(secondStudent);

        int ageMin = 20;
        int ageMax = 40;

        // when
        when(studentService.findByAgeBetween(ageMin, ageMax)).thenReturn(students);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/ageBetween" + "?ageMin=" + ageMin + "&ageMax=" + ageMax))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("firstName")))
                .andExpect(jsonPath("$[1].name", Matchers.equalTo("secondName")));
    }

}
