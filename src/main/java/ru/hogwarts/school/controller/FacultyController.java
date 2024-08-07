package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.Impl.FacultyServiceImpl;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping(path = "faculty")
public class FacultyController {

    FacultyServiceImpl facultyServiceImpl;

    public FacultyController(FacultyServiceImpl facultyServiceImpl) {
        this.facultyServiceImpl = facultyServiceImpl;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty addedFaculty = facultyServiceImpl.createFaculty(faculty);
        return ResponseEntity.ok(addedFaculty);
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> readFaculty(@PathVariable Long id) {
        Faculty findedFaculty = facultyServiceImpl.readFaculty(id);
        if (findedFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(findedFaculty);
    }

    @PutMapping("{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, Faculty faculty) {
        Faculty editedFaculty = facultyServiceImpl.updateFaculty(id, faculty);
        if (editedFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(editedFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        Faculty deletedFaculty = facultyServiceImpl.deleteFaculty(id);
        if (deletedFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(deletedFaculty);
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> findColorFaculty(@RequestParam String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyServiceImpl.findColorFaculty(color));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

}
