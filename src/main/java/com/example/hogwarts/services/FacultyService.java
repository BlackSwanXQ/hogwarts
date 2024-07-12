package com.example.hogwarts.services;

import com.example.hogwarts.exceptions.FacultyNotFoundException;
import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty create(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty get(long id) {
        if (facultyRepository.findById(id).isPresent()) {
            return facultyRepository.findById(id).get();
        }
        throw new FacultyNotFoundException(id);
    }

    public void update(long id, Faculty faculty) {
        if (facultyRepository.findById(id).isPresent()) {
            faculty.setId(id);
            facultyRepository.save(faculty);
            return;
        }
        throw new FacultyNotFoundException(id);
//           Faculty oldFaculty = faculties.get(id);
//           oldFaculty.setName(faculty.getName());
//           oldFaculty.setColor(faculty.getColor());
    }

    public void remove(long id) {
        if (facultyRepository.findById(id).isPresent()) {
            facultyRepository.deleteById(id);
            return;
        }
        throw new FacultyNotFoundException(id);
    }

    public List<Faculty> filterByColorFaculty(String color) {
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }
}
