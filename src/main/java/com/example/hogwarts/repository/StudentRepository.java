package com.example.hogwarts.repository;

import com.example.hogwarts.model.Faculty;
import com.example.hogwarts.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findByAge(int age);
}
