package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.Impl.FacultyServiceImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "faculty")
public class FacultyController {

    FacultyServiceImpl facultyService;

    public FacultyController(FacultyServiceImpl facultyServiceImpl) {
        this.facultyService = facultyServiceImpl;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty addedFaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.ok(addedFaculty);
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> readFaculty(@PathVariable Long id) {
        Faculty findedFaculty = facultyService.readFaculty(id);
        if (findedFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(findedFaculty);
    }

    @PutMapping("{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, Faculty faculty) {
        Faculty editedFaculty = facultyService.updateFaculty(id, faculty);
        if (editedFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(editedFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        Faculty deletedFaculty = facultyService.deleteFaculty(id);
        if (deletedFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(deletedFaculty);
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> findColorFaculty(@RequestParam String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.findColorFaculty(color));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping(path = "nameOrColor")
    public ResponseEntity<Collection<Faculty>> findByNameIgnoreCaseOrColorIgnoreCase
            (@RequestParam(required = false) String name,
             @RequestParam(required = false) String color) {
        if ((name != null && !name.isBlank()) || (color != null && !color.isBlank())) {
            return ResponseEntity.ok(facultyService.findByNameIgnoreCaseOrColorIgnoreCase(name, color));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping(path = "{id}/student")
    public ResponseEntity<List<Student>> getStudent(@PathVariable Long id) {
        List<Student> foundStudents = facultyService.getStudents(id);
        if (foundStudents == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudents);
    }

}
