package com.example.hogwarts.repository;

import com.example.hogwarts.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findAllByAge(int age);
    List<Student> findByAgeBetween(int maxAge, int minAge);
    List<Student> findAllByFaculty_Id(long facultyId);
}
