package com.example.hogwarts.controllers;

import com.example.hogwarts.entity.Faculty;
import com.example.hogwarts.entity.Student;
import com.example.hogwarts.repository.AvatarRepository;
import com.example.hogwarts.repository.FacultyRepository;
import com.example.hogwarts.repository.StudentRepository;
import com.example.hogwarts.services.AvatarService;
import com.example.hogwarts.services.FacultyService;
import com.example.hogwarts.services.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    FacultyRepository facultyRepository;

    @SpyBean
    FacultyService facultyService;

    @MockBean
    AvatarService avatarService;

    @MockBean
    StudentService studentService;
    @MockBean
    StudentRepository studentRepository;


    @InjectMocks
    FacultyController facultyController;

    @Test
    void createTest() throws Exception {
        Long id = 1L;
        String name = "fac1";
        String color = "red";

        JSONObject userObject = new JSONObject();
        userObject.put("name", name);
        userObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(userObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void getTest() throws Exception {
        Long id = 1L;
        String name = "fac1";
        String color = "red";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void updateTest() throws Exception {
        JSONObject userObject = new JSONObject();
        userObject.put("name", "fac2");
        userObject.put("color", "green");

        when(facultyRepository.findById(2L)).thenReturn(Optional.of(new Faculty(2L, "fac22", "blue")));
        Faculty faculty = new Faculty(2L, "fac2", "green");
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteStudent() throws Exception {
        Long id = 2L;
        String name = "fac1";
        String color = "red";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findById(2L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void findByColorOrNameTest() throws Exception {

        when(facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(anyString(), anyString()))
                .thenReturn(List.of(
                        new Faculty(1L, "fac1", "red"),
                        new Faculty(2L, "fac2", "red"),
                        new Faculty(3L, "fac3", "red")
                ));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/colorOrName?colorOrName=fac1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("fac1"));

    }

    @Test
    void findStudentsByFacultyId() throws Exception {

        when(studentRepository.findAllByFaculty_Id(2L)).thenReturn(List.of(
                new Student(30, "st1", 1L),
                new Student(34, "st2", 2L),
                new Student(42, "st3", 3L)
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/2/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].name").value("st2"));
    }

}
