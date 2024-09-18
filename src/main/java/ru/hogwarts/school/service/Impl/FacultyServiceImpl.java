package ru.hogwarts.school.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty readFaculty(long id) {
        logger.info("Was invoked method for get faculty by id {}", id);
        return facultyRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public Faculty updateFaculty(Long id, Faculty faculty) {
        logger.info("Was invoked method for update faculty");

        Optional<Faculty> facultyFromDB = facultyRepository.findById(id);
        if (facultyFromDB.isPresent()) {
            Faculty temp = facultyFromDB.get();
            temp.setName(faculty.getName());
            temp.setColor(faculty.getColor());
            facultyRepository.save(temp);
        }
        logger.debug("Faculty by id {} should be updated", id);
        return null;
    }

    @Override
    public Faculty deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty");
        logger.debug("Faculty by id {} should be deleted", id);
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
        logger.info("Was invoked method for find faculty by color");
        return facultyRepository.findAllByColor(color);
    }

    @Override
    public List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color) {
        logger.info("Was invoked method for find faculty by name or color");
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    @Override
    public List<Student> getStudents(Long facultyId) {
        logger.info("Was invoked method for get students by facultyId {}", facultyId);
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudents)
                .orElse(null);
    }

}
