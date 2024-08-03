package ru.hogwarts.school.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty readFaculty(long id) {
        return facultyRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public Faculty updateFaculty(Long id, Faculty faculty) {
        return facultyRepository
                .findById(id)
                .map(facultyFromDB -> {
                    facultyFromDB.setName(faculty.getName());
                    facultyFromDB.setColor(faculty.getColor());
                    facultyRepository.save(facultyFromDB);
                    return facultyFromDB;
                })
                .orElse(null);
    }

    @Override
    public Faculty deleteFaculty(long id) {
        return facultyRepository
                .findById(id)
                .map(faculty -> {
                    facultyRepository.deleteById(id);
                    return faculty;
                })
                .orElse(null);
    }

    @Override
    public List<Faculty> findColorFaculty(String color) {
        return facultyRepository.findAllByColor(color);
    }

}
