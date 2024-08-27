package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentRepository studentRepository;
    @SpyBean
    private StudentService studentService;
//    @InjectMocks
//    private StudentController studentController;


    @Test
    void shouldGetStudent() throws Exception {
        // given
        Long studentId = 1L;
        Student student = new Student("Ivan", 20);

        when(studentService.get(studentId)).thenReturn(student);

        // when
        ResultActions perform = mockMvc.perform(get("/student/{id}", studentId));

        // then
        perform
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());

    @Test
    public void saveUserTest() throws Exception {
        Long id = 1L;
        String name = "Bob";
        int age = 15;

        JSONObject userObject = new JSONObject();
        userObject.put("name", name);
        userObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

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
    void getStudentTest() throws Exception {
        Long id = 1L;
        String name = "Oleg";
        int age = 15;

        Student student = new Student(id, name, age);

        when(studentService.readStudent(id)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + id))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(status().isOk());
    }

    @Test
    void updateStudentTest() throws Exception {
        Long id = 1L;
        String name = "Oleg";
        int age = 15;
        int newAge = 16;

        Student student = new Student(id, name, age);
        Student updatedStudent = new Student(id, name, newAge);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", name);
        jsonObject.put("age", newAge);

        when(studentService.updateStudent(eq(id), any(Student.class))).thenReturn(updatedStudent);
        when(studentService.readStudent(id)).thenReturn(updatedStudent);
        when(studentRepository.save(student)).thenReturn(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/update/" + id)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(newAge));
    }

    @Test
    void updateStudentNegativeTest() throws Exception {
        Long id = 1L;
        String name = "Oleg";
        int age = 15;
        int newAge = 16;

        Student student = new Student(id, name, age);
        Student updatedStudent = new Student(id, name, newAge);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", name);
        jsonObject.put("age", newAge);

        when(studentService.readStudent(id)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/update/" + id)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteStudent() throws Exception {
        long id = 1L;
        String name = "Bob";
        int age = 37;
        Student student = new Student(id, name, age);

        when(studentService.readStudent(id)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/delete/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}