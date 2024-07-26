package com.example.hogwarts.controllers;

import com.example.hogwarts.entity.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    StudentController studentController;

    @Autowired
    TestRestTemplate restTemplate;


    @BeforeEach
    void beforeEach() {
    }

    @AfterEach
    void afterEach() {
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void greet() {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:"
                + port + "/student/greet", String.class)).isEqualTo("Hello, World!");
    }

    @Test
    void create() {
        Student student = new Student();
        student.setName("John");
        Assertions.assertThat(this.restTemplate.postForObject("http://localhost:"
                + port + "/student", student, Student.class)).isNotNull();
    }

    @Test
    void get() {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:"
                + port + "/student/7", Student.class)).isNotNull();
    }

    @Test
    void update() {
        Student student = new Student();
        student.setName("John333");
        HttpEntity<Student> request = new HttpEntity<Student>(student);
        ResponseEntity<Student> response = this.restTemplate.exchange("http://localhost:"
                + port + "/student/{id}", HttpMethod.PUT, request, Student.class, 7);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteStudent() {
        Student student = new Student();
        HttpEntity<Student> request = new HttpEntity<>(student);
        ResponseEntity<Student> response = this.restTemplate.exchange("http://localhost:"
                + port + "/student/{id}", HttpMethod.DELETE, request, Student.class, 43);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void filterByStudentAgeTest() {
        Student[] expected = restTemplate.getForObject("http://localhost:"
                + port + "/student/age?age=20", Student[].class);
        Assertions.assertThat(expected.length).isEqualTo(2);
        Assertions.assertThat(expected[0].getAge()).isEqualTo(20);
    }


    @Test
    void filterByRangeAge() {
        Student[] expected = restTemplate.getForObject("http://localhost:"
                + port + "/student/betweenAge?minAge=30&maxAge=40", Student[].class);
        Assertions.assertThat(expected.length).isEqualTo(3);
        Assertions.assertThat(expected[0].getAge()).isBetween(30, 40);
        Assertions.assertThat(expected[1].getAge()).isBetween(30, 40);
        Assertions.assertThat(expected[2].getAge()).isBetween(30, 40);
    }

    @Test
    void findStudentFaculty() {
        Student expected = restTemplate.getForObject("http://localhost:"
                + port + "/student/5/faculty", Student.class);
        Assertions.assertThat(expected.getId()).isEqualTo(4);
    }

    @Test
    void getAvatarFromFs() {
        Student student = new Student();
        HttpEntity<Student> request = new HttpEntity<Student>(student);
        ResponseEntity<byte[]> response = this.restTemplate.exchange("http://localhost:"
                + port + "/student/{id}/avatar-from-fs", HttpMethod.GET, request, byte[].class, 3);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAvatarFormDb() {
        Student student = new Student();
        HttpEntity<Student> request = new HttpEntity<Student>(student);
        ResponseEntity<byte[]> response = this.restTemplate.exchange("http://localhost:"
                + port + "/student/{id}/avatar-from-db", HttpMethod.GET, request, byte[].class, 4);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}