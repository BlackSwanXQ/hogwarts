package com.example.hogwarts.repository;

import com.example.hogwarts.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FacultyRepository extends JpaRepository<Faculty,Long> {
    List<Faculty> findByColor(String color);
    List<Faculty> findAllByColorIgnoreCaseOrNameIgnoreCase(String color, String name);

}
