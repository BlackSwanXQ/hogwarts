package com.example.hogwarts.controllers;

import com.example.hogwarts.entity.Faculty;
import com.example.hogwarts.entity.Student;
import com.example.hogwarts.services.FacultyService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.create(faculty);
    }

    @GetMapping("/{id}")
    public Faculty get(@PathVariable long id) {
        return facultyService.get(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id,
                       @RequestBody Faculty faculty) {
        facultyService.update(id, faculty);
    }

    @DeleteMapping("/{id}")
    public Faculty deleteFaculty(@PathVariable long id) {
       return facultyService.remove(id);
    }


    @GetMapping(params = "color")
    public List<Faculty> filterByColorFaculty(@RequestParam String color) {
        return facultyService.filterByColorFaculty(color);
    }

    @GetMapping("/all")
    public Collection<Faculty> getAllFaculty() {
        return facultyService.getAllFaculty();
    }

//    @GetMapping(params = "colorOrName")
    @GetMapping("/colorOrName")
    public List<Faculty> findByColorOrName(@RequestParam String colorOrName) {
        return facultyService.findByColorOrName(colorOrName);
    }
    @GetMapping("/{id}/students")
    public List<Student> findStudentsByFacultyId(@PathVariable long id) {
        return facultyService.findStudentsByFacultyId(id);
    }
}
