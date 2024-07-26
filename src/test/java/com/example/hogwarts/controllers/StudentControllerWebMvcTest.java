package com.example.hogwarts.controllers;


import com.example.hogwarts.entity.Avatar;
import com.example.hogwarts.entity.Faculty;
import com.example.hogwarts.entity.Student;
import com.example.hogwarts.repository.AvatarRepository;
import com.example.hogwarts.repository.FacultyRepository;
import com.example.hogwarts.repository.StudentRepository;
import com.example.hogwarts.services.AvatarService;
import com.example.hogwarts.services.FacultyService;
import com.example.hogwarts.services.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import netscape.javascript.JSObject;
import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.util.Pair;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StudentRepository studentRepository;

    @MockBean
    FacultyRepository facultyRepository;

    @MockBean
    AvatarRepository avatarRepository;

    @MockBean
    FacultyService facultyService;

    @SpyBean
    StudentService studentService;

    @SpyBean
    AvatarService avatarService;

    @InjectMocks
    StudentController studentController;

    @Test
    void createTest() throws Exception {
        Long id = 1L;
        String name = "st10";
        int age = 30;

        JSONObject userObject = new JSONObject();
        userObject.put("name", name);
        userObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(userObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age));
    }

    @Test
    void getTest() throws Exception {
        Long id = 1L;
        String name = "st10";
        int age = 30;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age));
    }

    @Test
    void updateTest() throws Exception {
        JSONObject userObject = new JSONObject();
        userObject.put("name", "st33");
        userObject.put("age", 33L);

        when(studentRepository.findById(33L)).thenReturn(Optional.of(new Student(23, "st42", 33L)));
        Student student = new Student(33, "st33", 33L);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/33")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteStudent() throws Exception {
        Long id = 2L;
        String name = "st10";
        int age = 30;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.findById(2L)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age));
    }

    @Test
    void filterByStudentAgeTest() throws Exception {

        when(studentRepository.findAllByAge(20)).thenReturn(List.of(
                new Student(20, "st33", 33L),
                new Student(20, "st44", 44L),
                new Student(20, "st55", 55L)
        ));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age?age=20"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(44))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("st44"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(20));
    }

    @Test
    void filterByRangeAge() throws Exception {
        when(studentRepository.findByAgeBetween(30, 40)).thenReturn(List.of(
                new Student(33, "st33", 33L),
                new Student(44, "st44", 44L),
                new Student(55, "st55", 55L)
        ));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/betweenAge?minAge=30&maxAge=40"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(44))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("st44"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(44L));
    }

    @Test
    void findStudentFacultyTest() throws Exception {
        Student f1 = new Student();
        f1.setFaculty(new Faculty(1L, "fac1", "red"));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(f1));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("fac1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("red"));
    }

}









