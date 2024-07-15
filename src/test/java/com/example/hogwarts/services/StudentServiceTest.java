package com.example.hogwarts.services;

import com.example.hogwarts.exceptions.StudentNotFoundException;
import com.example.hogwarts.entity.Student;
import com.example.hogwarts.repository.FacultyRepository;
import com.example.hogwarts.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void beforeEach() {
    }

    @AfterEach
    void afterEach() {
    }

    @Test
    void createTest() {
        Student expected = new Student(40, "Ivanov6", 6L);
        when(studentRepository.save(expected)).thenReturn(expected);
        Student actual = studentService.create(expected);
        assertThat(studentService.create(actual))
                .isEqualTo(expected);
    }

    @Test
    void getTest() {
        Student expected = new Student(40, "Ivanov6", 1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(expected));
        assertThat(studentService.get(1L)).isEqualTo(expected);
    }

    @Test
    void getExceptionTest() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatExceptionOfType(StudentNotFoundException.class)
                .isThrownBy(() -> studentService.get(1L));
    }

    @Test
    void updateTest() {
        when(studentRepository.save(new Student(40, "Ivanov6", 9L)))
                .thenReturn(new Student(40, "Ivanov7", 9L));
        assertThat(studentRepository.save(new Student(40, "Ivanov6", 9L)))
                .isEqualTo(new Student(40, "Ivanov7", 9L));
    }

    @Test
    void updateExceptionTest() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatExceptionOfType(StudentNotFoundException.class)
                .isThrownBy(() -> studentService.get(1L));
    }

    @Test
    void remove() {
        doNothing().when(studentRepository).deleteById(1L);
        studentRepository.deleteById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void removeExceptionTest() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        studentRepository.deleteById(1L);
        assertThatExceptionOfType(StudentNotFoundException.class)
                .isThrownBy(() -> studentService.get(1L));
    }

}