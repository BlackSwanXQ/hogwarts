package com.example.hogwarts.controllers;

import com.example.hogwarts.entity.Faculty;
import com.example.hogwarts.entity.Student;
import com.example.hogwarts.repository.AvatarRepository;
import com.example.hogwarts.repository.FacultyRepository;
import com.example.hogwarts.repository.StudentRepository;
import com.example.hogwarts.services.AvatarService;
import com.example.hogwarts.services.FacultyService;
import com.example.hogwarts.services.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.postgresql.hostchooser.HostRequirement.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    FacultyRepository facultyRepository;

    @MockBean
    StudentRepository studentRepository;

    @SpyBean
    FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();


//    @Test
//    void createTest() throws Exception {
//        JSONObject userObject = new JSONObject();
//        userObject.put("name", "fac1");
//        userObject.put("color", "red");
//
//        Faculty faculty = new Faculty(1L, "fac1", "red");
//
//        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/faculty")
//                        .content(userObject.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.name").value("fac1"))
//                .andExpect(jsonPath("$.color").value("red"));
//    }
//
//    @Test
//    void getTest() throws Exception {
//        Faculty faculty = new Faculty(1L, "fac1", "red");
//        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/faculty/1")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.name").value("fac1"))
//                .andExpect(jsonPath("$.color").value("red"));
//    }

    @Test
    void updatePositiveTest() throws Exception {

//        when(facultyRepository.findById(2L)).thenReturn(Optional.of(new Faculty(2L, "fac22", "blue")));
//        Faculty faculty = new Faculty(2L, "fac2", "green");
//        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .put("/faculty/2")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(faculty)))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("fac2"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2L))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("green"));

        long id = 1L;
        Faculty oldFaculty = new Faculty();
        oldFaculty.setId(id);
        oldFaculty.setName("fac1");
        oldFaculty.setColor("red");

        Faculty newFaculty = new Faculty();
        newFaculty.setId(id);
        newFaculty.setName("fac2");
        newFaculty.setColor("green");

        when(facultyRepository.findById(any())).thenReturn(Optional.of(oldFaculty));
        when(facultyRepository.save(any())).thenReturn(newFaculty);

        mockMvc.perform(MockMvcRequestBuilders.put("/faculty/{id}", id)
                .param("id", String.valueOf(id))
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newFaculty))
        ).andExpect(result -> {
            MockHttpServletResponse response = result.getResponse();
            Faculty responseFaculty = objectMapper.readValue(result.getResponse().getContentAsString(), Faculty.class);
            Assertions.assertThat(responseFaculty).usingRecursiveComparison().isEqualTo(newFaculty);
            Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        });

    }

//    @Test
//    void deleteStudent() throws Exception {
//        Faculty faculty = new Faculty(2L,"fac2","red");
//        when(facultyRepository.findById(2L)).thenReturn(Optional.of(faculty));
//        mockMvc.perform(MockMvcRequestBuilders
//                        .delete("/faculty/2")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(2L))
//                .andExpect(jsonPath("$.name").value("fac2"))
//                .andExpect(jsonPath("$.color").value("red"));
//    }
//
//    @Test
//    void findByColorOrNameTest() throws Exception {
//
//        when(facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(anyString(), anyString()))
//                .thenReturn(List.of(
//                        new Faculty(1L, "fac1", "red"),
//                        new Faculty(2L, "fac2", "red"),
//                        new Faculty(3L, "fac3", "red")
//                ));
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/faculty/colorOrName?colorOrName=fac1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("fac1"))
//                .andExpect(jsonPath("$[0].color").value("red"))
//                .andExpect(jsonPath("$[0].id").value(1L));
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/faculty/colorOrName?colorOrName=red"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[1].name").value("fac2"))
//                .andExpect(jsonPath("$[1].color").value("red"))
//                .andExpect(jsonPath("$[1].id").value(2L));
//
//    }
//
//    @Test
//    void findStudentsByFacultyId() throws Exception {
//
//        when(studentRepository.findAllByFaculty_Id(2L)).thenReturn(List.of(
//                new Student(30, "st1", 2L),
//                new Student(34, "st2", 2L),
//                new Student(42, "st3", 2L)
//        ));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/2/students"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[1].name").value("st2"))
//                .andExpect(jsonPath("$[1].id").value(2L))
//                .andExpect(jsonPath("$[1].age").value(34));
//    }

}
