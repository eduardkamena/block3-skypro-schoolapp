package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.Impl.StudentServiceImpl;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping(path = "student")
public class StudentController {

    StudentServiceImpl studentService;

    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentService = studentServiceImpl;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student addedStudent = studentService.createStudent(student);
        return ResponseEntity.ok(addedStudent);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> readStudent(@PathVariable Long id) {
        Student foundStudent = studentService.readStudent(id);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @PutMapping("{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, Student student) {
        Student editedStudent = studentService.updateStudent(id, student);
        if (editedStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(editedStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        Student deletedStudent = studentService.deleteStudent(id);
        if (deletedStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(deletedStudent);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> findAgeStudent(@RequestParam int age) {
        if (age > 0) {
            return ResponseEntity.ok(studentService.findAgeStudent(age));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping(path = "ageBetween")
    public ResponseEntity<Collection<Student>> findByAgeBetween(@RequestParam int ageMin,
                                                                @RequestParam int ageMax) {
        if (ageMin > 0 && ageMax > 0) {
            return ResponseEntity.ok(studentService.findByAgeBetween(ageMin, ageMax));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping(path = "{id}/faculty")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty foundFaculty = studentService.getFaculty(id);
        if (foundFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

}
