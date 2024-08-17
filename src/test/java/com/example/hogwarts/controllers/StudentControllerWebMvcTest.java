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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    FacultyRepository facultyRepository;
    @MockBean
    private AvatarService avatarService;

    @SpyBean
    private StudentService studentService;


    @Test
    void createTest() throws Exception {

        JSONObject userObject = new JSONObject();
        userObject.put("name", "st10");
        userObject.put("age", 30);
        Student student10 = new Student(30, "st10", null);
        when(studentRepository.save(any())).thenReturn(student10);
        mockMvc.perform(MockMvcRequestBuilders.post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userObject.toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(student10.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(student10.getAge()));
    }

    @Test
    void getTest() throws Exception {
        Student student = new Student(30, "st10", 1L);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("st10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(30));
    }

    @Test
    void updateTest() throws Exception {

        when(studentRepository.findById(2L)).thenReturn(Optional.of(new Student(23, "st42", 2L)));
        Student student = new Student(33, "st33", 2L);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("st33"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(33))
                .andDo(print());
    }

    @Test
    void deleteStudent() throws Exception {
        Student student = new Student(30, "st10", 2L);
        when(studentRepository.findById(2L)).thenReturn(Optional.of(student));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("st10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(30))
                .andDo(print());
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(44))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("st44"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(20));
    }

    @Test
    void filterByRangeAge() throws Exception {

        when(studentRepository.findByAgeBetween(20, 40)).thenReturn(List.of(
                new Student(23, "st33", 1L),
                new Student(40, "st44", 2L),
                new Student(39, "st42", 3L)
        ));
        mockMvc.perform(MockMvcRequestBuilders.get("/student/betweenAge?minAge=20&maxAge=40"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("st44"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(40));
    }

    @Test
    void findStudentFacultyTest() throws Exception {
        Student f1 = new Student();
        f1.setFaculty(new Faculty(1L, "fac1", "red"));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(f1));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("fac1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("red"));
    }

}









