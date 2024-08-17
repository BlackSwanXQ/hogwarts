package com.example.hogwarts.controllers;

import com.example.hogwarts.entity.Faculty;
import com.example.hogwarts.entity.Student;
import com.example.hogwarts.repository.FacultyRepository;
import com.example.hogwarts.repository.StudentRepository;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import net.datafaker.providers.base.Color;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerRestTemplateTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    FacultyRepository facultyRepository;

    private final Faker faker = new Faker();
    private final List<Student> students = new ArrayList<>(10);

    @BeforeEach
    void beforeEach() {
        Faculty faculty1 = createFaculty();
        Faculty faculty2 = createFaculty();

        createStudents(faculty1, faculty2);
    }

    private Faculty createFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());
        return facultyRepository.save(faculty);
    }

    private void createStudents(Faculty... faculties) {
        students.clear();
        Stream.of(faculties)
                .forEach(faculty -> students.addAll(
                        studentRepository.saveAll(Stream.generate(() -> {
                                    Student student = new Student();
                                    student.setFaculty(faculty);
                                    student.setName(faker.harryPotter().character());
                                    student.setAge(faker.random().nextInt(11, 18));
                                    return student;
                                })
                                .limit(5)
                                .collect(Collectors.toList()))));
    }


    @AfterEach
    void afterEach() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
//        students.clear();
    }

    private String buildUrl(String urlStartsWithSlash) {
        return "http://localhost:%d%s".formatted(port, urlStartsWithSlash);
    }

    @Test
    void createStudentWithoutFacultyPositive() throws JsonProcessingException {
        Student student = new Student();
        student.setAge(faker.random().nextInt(11, 18));
        student.setName(faker.harryPotter().character());

        createStudentPositive(student);
    }

    @Test
    void createStudentWithFacultyPositive() throws JsonProcessingException {
        Student student = new Student();
        student.setAge(faker.random().nextInt(11, 18));
        student.setName(faker.harryPotter().character());
        Faculty randomFacultyFromDB = facultyRepository.findAll(PageRequest.of(faker.random().nextInt(0, 1) /* случайный номер страницы от 0 до 1, т.к всего 2 факультета */
                        , 1))
                .getContent().get(0);
        student.setFaculty(randomFacultyFromDB);
        createStudentPositive(student);
    }


    private void createStudentPositive(Student student) {
        ResponseEntity<Student> responseEntity = restTemplate.postForEntity(buildUrl("/student"),
                student,
                Student.class
        );

        Student created = responseEntity.getBody();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(created).isNotNull();
        Assertions.assertThat(created).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(student);
        Assertions.assertThat(created.getId()).isNotNull();

        Optional<Student> fromDB = studentRepository.findById(created.getId());
        Assertions.assertThat(fromDB).isPresent();
        Assertions.assertThat(fromDB.get())
                .usingRecursiveComparison()
                .isEqualTo(created);
    }

    @Test
    void createStudentWithoutFacultyWhichNotExistNegative() {
        Student student = new Student();
        student.setAge(faker.random().nextInt(11, 18));
        student.setName(faker.harryPotter().character());

        Faculty faculty = new Faculty();
        faculty.setId(-1L);
        student.setFaculty(faculty);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                buildUrl("/student"),
                student,
                String.class
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("Faculty with id = %d not found".formatted(-1));
    }

    @Test
    void getStudentPositive() {
        Random rand = new Random();
        long idRand = studentRepository.findAll().get(rand.nextInt(10)).getId();
        ResponseEntity<Student> actual = restTemplate.getForEntity("/student/" + idRand, Student.class);
        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(studentRepository.findAll().contains(actual.getBody())).isEqualTo(true);
        Assertions.assertThat(actual.getBody()).isIn(studentRepository.findAll());
        Assertions.assertThat(actual.getBody()).isEqualTo(studentRepository.findById(actual.getBody().getId()).get());
    }

    //    @Test
//    void update() {
//        Student student10 = new Student(11, "st11", 2L);
//        HttpEntity<Student> request = new HttpEntity<Student>(student10);
//        ResponseEntity<Student> forEntity = restTemplate
//                .exchange("/student/2", HttpMethod.PUT, request, Student.class);
//        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
//        Student actual = forEntity.getBody();
//        Assertions.assertThat(actual).isEqualTo(studentRepository.findById(actual.getId()).get());
//    }
//
//    @Test
//    void deleteStudent() {
//        Student student = new Student();
////        HttpEntity<Student> request = new HttpEntity<>(student);
//        ResponseEntity<Student> forEntity = restTemplate
//                .exchange("/student/3", HttpMethod.DELETE, null, Student.class);
//        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
//        Student actual = forEntity.getBody();
//        Assertions.assertThat(studentRepository.findById(3L)).isEmpty();
//        Assertions.assertThat(actual).isEqualTo(student3);
//    }
//
//    @Test
//    void filterByStudentAgeTest() {
////        ResponseEntity<Student[]> forEntity = restTemplate.getForEntity("/student/age?age=30", Student[].class);
//        ResponseEntity<Student[]> forEntity = restTemplate
//                .exchange("/student/age?age=30", HttpMethod.GET, null, Student[].class);
//        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
//        Assertions.assertThat(forEntity.getBody()).hasSize(2);
//        Assertions.assertThat(forEntity.getBody()).containsExactlyInAnyOrder(student1, student2);
//
//    }
//
    @Test
    void findByAgeBetweenTest() {
        int minAge = faker.random().nextInt(11, 18);
        int maxAge = faker.random().nextInt(minAge, 18);

        List<Student> expected = students.stream()
                .filter(student -> student.getAge() >= minAge && student.getAge() <= maxAge)
                .toList();

        ResponseEntity<List<Student>> responseEntity = restTemplate.exchange(
                buildUrl("/student/betweenAge?minAge={minAge}&maxAge={maxAge}"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                Map.of("minAge", minAge, "maxAge", maxAge));
        List<Student> actual = responseEntity.getBody();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actual).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    @Test
    void findFacultyPositive() {
        Student student = students.get(faker.random().nextInt(students.size()));
        ResponseEntity<Faculty> responseEntity = restTemplate.getForEntity(
                buildUrl("/student/{id}/faculty"),
                Faculty.class,
                Map.of("id", student.getId())
        );

        Faculty actual = responseEntity.getBody();

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actual).usingRecursiveComparison()
                .isEqualTo(student.getFaculty());

    }

    @Test
    void findFacultyNegative() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                buildUrl("/student/{id}/faculty"),
                String.class,
                Map.of("id", -1)
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("Student with id = %d not found".formatted(-1));
    }
}