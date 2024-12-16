package com.example.hogwarts.controllers;

import com.example.hogwarts.entity.Faculty;
import com.example.hogwarts.entity.Student;
import com.example.hogwarts.repository.FacultyRepository;
import com.example.hogwarts.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerRestTemplateTest {


    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TestRestTemplate restTemplate;

    Faculty faculty1 = new Faculty(1L, "fac1", "red");
    Faculty faculty2 = new Faculty(2L, "fac1", "red");
    Faculty faculty3 = new Faculty(3L, "fac3", "green");

    @BeforeEach
    void beforeEach() {
        facultyRepository.save(faculty1);
        facultyRepository.save(faculty2);
        facultyRepository.save(faculty3);
    }

    @AfterEach
    void afterEach() {
        facultyRepository.deleteAll();
    }


    @Test
    void create() {
        Faculty faculty4 = new Faculty(4L, "fac4", "yellow");
        ResponseEntity<Faculty> forEntity = restTemplate.postForEntity("/faculty", faculty4, Faculty.class);
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Faculty actual = forEntity.getBody();
        Assertions.assertThat(actual).isEqualTo(facultyRepository.findById(actual.getId()).get());
    }

    @Test
    void get() {
        ResponseEntity<Faculty> forEntity = restTemplate.getForEntity("/faculty/2", Faculty.class);
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(forEntity.getBody()).isEqualTo(faculty2);
    }

    @Test
    void update() {
        Faculty faculty2 = new Faculty(2L, "fac22", "black");
        HttpEntity<Faculty> request = new HttpEntity<Faculty>(faculty2);
        ResponseEntity<Faculty> forEntity = restTemplate
                .exchange("/faculty/2", HttpMethod.PUT, request, Faculty.class);
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(facultyRepository.findById(2L).get()).isEqualTo(faculty2);
    }

    @Test
    void deleteFaculty() {
        Faculty faculty = new Faculty();
//        HttpEntity<Faculty> request = new HttpEntity<>(faculty);
        ResponseEntity<Faculty> forEntity = restTemplate
                .exchange("/faculty/3", HttpMethod.DELETE, null, Faculty.class);
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Faculty actual = forEntity.getBody();
        Assertions.assertThat(facultyRepository.findById(3L)).isEmpty();
        Assertions.assertThat(actual).isEqualTo(faculty3);
    }

    @Test
    void findByColorOrName() {

        ResponseEntity<Faculty[]> forEntity = restTemplate
                .exchange("/faculty/colorOrName?colorOrName=red", HttpMethod.GET, null, Faculty[].class);
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(forEntity.getBody()).hasSize(2);
        Assertions.assertThat(forEntity.getBody()).containsExactlyInAnyOrder(faculty1, faculty2);

        ResponseEntity<Faculty[]> forEntity2 = restTemplate
                .exchange("/faculty/colorOrName?colorOrName=fac1", HttpMethod.GET, null, Faculty[].class);
        Assertions.assertThat(forEntity2.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(forEntity2.getBody()).hasSize(2);
        Assertions.assertThat(forEntity.getBody()).containsExactlyInAnyOrder(faculty1, faculty2);
    }


}
