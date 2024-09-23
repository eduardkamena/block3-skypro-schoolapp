package ru.hogwarts.school.service.Impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;

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

        Optional<Faculty> facultyFromDB = facultyRepository.findById(id);
        if (facultyFromDB.isPresent()) {
            Faculty temp = facultyFromDB.get();
            temp.setName(faculty.getName());
            temp.setColor(faculty.getColor());
            facultyRepository.save(temp);
        }
        return null;
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

    @Override
    public List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    @Override
    public List<Student> getStudents(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudents)
                .orElse(null);
    }

}
