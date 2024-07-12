package com.example.hogwarts.services;

import com.example.hogwarts.exceptions.FacultyNotFoundException;
import com.example.hogwarts.exceptions.StudentNotFoundException;
import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.model.Student;
import com.example.hogwarts.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {


    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student get(long id) {
        if (studentRepository.findById(id).isPresent()) {
            return studentRepository.findById(id).get();
        }
        throw new StudentNotFoundException(id);
    }

    public void update(long id, Student student) {
        if (studentRepository.findById(id).isPresent()) {
            student.setId(id);
            studentRepository.save(student);
            return;
        }
        throw new StudentNotFoundException(id);
//           Faculty oldFaculty = faculties.get(id);
//           oldFaculty.setName(faculty.getName());
//              oldFaculty.setColor(faculty.getColor());
    }

    public void remove(long id) {
        if (studentRepository.findById(id).isPresent()) {
            studentRepository.deleteById(id);
            return;
        }
        throw new StudentNotFoundException(id);
    }

    public List<Student> filterByStudentAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
