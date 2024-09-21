package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
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
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
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

    @GetMapping(path = "count_amount")
    public ResponseEntity<Integer> getAllStudents() {
        Integer countAllStudents = studentService.getAllStudents();
        return ResponseEntity.ok(countAllStudents);
    }

    @GetMapping(path = "average_age")
    public ResponseEntity<Float> getAverageStudentAge() {
        Float averageAge = studentService.getAverageStudentAge();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping(path = "last_posted")
    public ResponseEntity<List<Student>> getLastPostedStudents() {
        List<Student> lastPostedStudents = studentService.getLastPostedStudents();
        return ResponseEntity.ok(lastPostedStudents);
    }

    @GetMapping(path = "name_starts_with_E")
    public ResponseEntity<List<String>> getStudentsNamesStartsWith() {
        List<String> startsWith = studentService.getStudentsNamesStartsWith();
        return ResponseEntity.ok(startsWith);
    }

    @GetMapping(path = "average_age_stream")
    public ResponseEntity<Double> getAverageAgeAllStudentsStream() {
        Double averageAge = studentService.getAverageAgeAllStudentsStream();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping(path = "print-parallel")
    public void printParallelThread() {
        studentService.printParallelThread();
    }

    @GetMapping(path = "print-synchronized")
    public void printSynchronizedThread() {
        studentService.printSynchronizedThread();
    }

}
