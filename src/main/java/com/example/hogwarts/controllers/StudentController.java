package com.example.hogwarts.controllers;

import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.model.Student;
import com.example.hogwarts.services.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable long id) {
        return studentService.get(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id,
                       @RequestBody Student student) {
        studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable long id) {
        studentService.remove(id);
    }

    @GetMapping
    public List<Student> filterByStudentAge (@RequestParam int age) {
        return studentService.filterByStudentAge(age);
    }

    @GetMapping("/all")
    public Collection<Student> getAllFaculty() {
        return studentService.getAllStudents();
    }
}
