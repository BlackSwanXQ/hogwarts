package com.example.hogwarts.controllers;

import com.example.hogwarts.entity.Faculty;
import com.example.hogwarts.entity.Student;
import com.example.hogwarts.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerRestTemplateTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    Student student1 = new Student(30, "st1", null);
    Student student2 = new Student(30, "st2", null);
    Student student3 = new Student(45, "st3", null);
    Student student4 = new Student(52, "st4", null);
    Student student5 = new Student(55, "st5", null);


    @BeforeEach
    void beforeEach() {
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        studentRepository.save(student4);
        studentRepository.save(student5);
    }

    @AfterEach
    void afterEach() {
        studentRepository.deleteAll();
    }

    @Test
    void create() {
        Student student10 = new Student(30, "st10", null);
        ResponseEntity<Student> forEntity = restTemplate.postForEntity("/student", student10, Student.class);
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Student actual = forEntity.getBody();
        Assertions.assertThat(actual).isEqualTo(studentRepository.findById(actual.getId()).get());
    }


    @Test
    void get() {
        ResponseEntity<Student> forEntity = restTemplate.getForEntity("/student/3", Student.class);
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(forEntity.getBody()).isEqualTo(student3);
    }

    @Test
    void update() {
        Student student10 = new Student(11, "st11", 2L);
        HttpEntity<Student> request = new HttpEntity<Student>(student10);
        ResponseEntity<Student> forEntity = restTemplate
                .exchange("/student/2", HttpMethod.PUT, request, Student.class);
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Student actual = forEntity.getBody();
        Assertions.assertThat(actual).isEqualTo(studentRepository.findById(actual.getId()).get());
    }

    @Test
    void deleteStudent() {
        Student student = new Student();
        HttpEntity<Student> request = new HttpEntity<>(student);
        ResponseEntity<Student> forEntity = restTemplate
                .exchange("/student/3", HttpMethod.DELETE, request, Student.class);
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Student actual = forEntity.getBody();
        Assertions.assertThat(studentRepository.findById(3L)).isEmpty();
        Assertions.assertThat(actual).isEqualTo(student3);
    }

    @Test
    void filterByStudentAgeTest() {
//        ResponseEntity<Student[]> forEntity = restTemplate.getForEntity("/student/age?age=30", Student[].class);
        ResponseEntity<Student[]> forEntity = restTemplate
                .exchange("/student/age?age=30", HttpMethod.GET, null, Student[].class);
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(forEntity.getBody()).hasSize(2);
        Assertions.assertThat(forEntity.getBody()).containsExactlyInAnyOrder(student1,student2);
    }

    @Test
    void filterByRangeAge() {
        ResponseEntity<Student[]> forEntity = restTemplate
                .exchange("/student/betweenAge?minAge=40&maxAge=60", HttpMethod.GET, null, Student[].class);
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(forEntity.getBody()).hasSize(3);
        Assertions.assertThat(forEntity.getBody()).containsExactlyInAnyOrder(student3,student4,student5);
    }

}