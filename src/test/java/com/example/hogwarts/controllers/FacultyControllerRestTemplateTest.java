package com.example.hogwarts.controllers;

import com.example.hogwarts.entity.Faculty;
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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    FacultyController facultyController;

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
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    void create() {
        Faculty faculty = new Faculty();
        faculty.setName("fac5");
        faculty.setColor("black");
        Assertions.assertThat(this.restTemplate.postForObject("http://localhost:"
                + port + "/faculty", faculty, Faculty.class)).isNotNull();
    }

    @Test
    void get() {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:"
                + port + "/faculty/2", Faculty.class)).isNotNull();
    }

    @Test
    void update() {
        Faculty faculty = new Faculty();
        faculty.setName("fac577");
        faculty.setColor("black");
        HttpEntity<Faculty> request = new HttpEntity<Faculty>(faculty);
        ResponseEntity<Faculty> response = this.restTemplate.exchange("http://localhost:"
                + port + "/faculty/{id}", HttpMethod.PUT, request, Faculty.class, 10);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteFaculty() {
        Faculty faculty = new Faculty();
        HttpEntity<Faculty> request = new HttpEntity<>(faculty);
        ResponseEntity<Student> response = this.restTemplate.exchange("http://localhost:"
                + port + "/faculty/{id}", HttpMethod.DELETE, request, Student.class, 10);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
    }

    @Test
    void findByColorOrName() {
        Faculty[] expected = restTemplate.getForObject("http://localhost:"
                + port + "/faculty/colorOrName?colorOrName=red", Faculty[].class);
        Assertions.assertThat(expected).contains(new Faculty(1L,"fac1","red"));

        Faculty[] expected2 = restTemplate.getForObject("http://localhost:"
                + port + "/faculty/colorOrName?colorOrName=fac2", Faculty[].class);
        Assertions.assertThat(expected2).contains(new Faculty(2L,"fac2","blue"));
    }


    @Test
    void findStudentsByFacultyId() {
        Student[] expected = restTemplate.getForObject("http://localhost:"
                + port + "/faculty/2/students", Student[].class);
        Assertions.assertThat(expected).contains(new Student(30,"st3",3L));
    }
}
