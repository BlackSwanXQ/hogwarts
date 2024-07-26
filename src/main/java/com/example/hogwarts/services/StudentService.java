package com.example.hogwarts.services;

import com.example.hogwarts.entity.Faculty;
import com.example.hogwarts.exceptions.FacultyNotFoundException;
import com.example.hogwarts.exceptions.StudentNotFoundException;
import com.example.hogwarts.entity.Student;
import com.example.hogwarts.repository.FacultyRepository;
import com.example.hogwarts.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {


    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student create(Student student) {
        Faculty faculty = null;
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> new FacultyNotFoundException(student.getFaculty().getId()));
        }
        student.setFaculty(faculty);
        student.setId(null);
        return studentRepository.save(student);
    }

    public Student get(long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }


    public void update(long id, Student student) {
        Student oldStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        Faculty faculty = null;
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> new FacultyNotFoundException(student.getFaculty().getId()));
        }
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        oldStudent.setFaculty(faculty);
        studentRepository.save(oldStudent);
    }

    public Student remove(long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return student;
    }

    public List<Student> filterByStudentAge(int age) {
        return studentRepository.findAllByAge(age);
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> filterByRangeAge(int ageMin, int ageMax) {
        return studentRepository.findByAgeBetween(ageMin, ageMax);
    }


    public Faculty findStudentsFaculty(long id) {
        return get(id).getFaculty();
    }


}
