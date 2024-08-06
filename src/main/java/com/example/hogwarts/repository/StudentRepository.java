package com.example.hogwarts.repository;

import com.example.hogwarts.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findAllByFaculty_Id(long facultyId);

    @Query(value = "SELECT COUNT(name) FROM student", nativeQuery = true)
    int getAmountStudents();

    @Query(value = "select avg(age) from student", nativeQuery = true)
    double getAverageScore();

    @Query(value = "select * from student order by id desc LIMIT 5", nativeQuery = true)
    List<Student> getLastStudents();
}
