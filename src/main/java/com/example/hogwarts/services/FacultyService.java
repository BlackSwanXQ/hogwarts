package com.example.hogwarts.services;

import com.example.hogwarts.entity.Student;
import com.example.hogwarts.exceptions.FacultyNotFoundException;
import com.example.hogwarts.entity.Faculty;
import com.example.hogwarts.exceptions.StudentNotFoundException;
import com.example.hogwarts.repository.FacultyRepository;
import com.example.hogwarts.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository,
                          StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public Faculty create(Faculty faculty) {
        faculty.setId(null);
        logger.info("Was invoked method for create Faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty get(long id) {
        logger.info("Was invoked method for get Faculty");
        return facultyRepository.findById(id)
                .orElseThrow(() -> { logger.error("There is not Faculty with id = " + id);
                    return new FacultyNotFoundException(id);});
    }

    public Faculty update(long id, Faculty faculty) {
        logger.info("Was invoked method for update Faculty with id = " + id);
        Faculty oldFaculty = facultyRepository.findById(id)
                .orElseThrow(() -> {logger.warn("There is not Faculty with id = " + id);
                    return new FacultyNotFoundException(id);});
        oldFaculty.setName(faculty.getName());
        oldFaculty.setColor(faculty.getColor());
        logger.debug("Faculty with id {} was updated",id);
        return facultyRepository.save(oldFaculty);
    }

    public Faculty remove(long id) {
        logger.info("Was invoked method for remove Faculty with id = " + id);
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> {logger.warn("There is not Faculty with id = " + id);
                    return new FacultyNotFoundException(id);});
        facultyRepository.delete(faculty);
        logger.debug("Faculty with id {} was removed",id);
        return faculty;
    }

    public List<Faculty> filterByColorFaculty(String color) {
        logger.info("Was invoked method for filterByColorFaculty: " + color);
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    public List<Faculty> findByColorOrName(String colorOrName) {
        logger.info("Was invoked method for findByColorOrName: " + colorOrName);
        return facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName);
    }


    public List<Student> findStudentsByFacultyId(long id) {
        logger.info("Was invoked method for findStudentsByFacultyId: " + id);
        return studentRepository.findAllByFaculty_Id(id);
    }
}
