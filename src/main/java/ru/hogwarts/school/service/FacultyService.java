package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.List;

public interface FacultyService {

    Faculty createFaculty(Faculty faculty);

    Faculty readFaculty(long id);

    Faculty updateFaculty(Long id, Faculty faculty);

    Faculty deleteFaculty(long id);

    List<Faculty> findColorFaculty(String color);

}
