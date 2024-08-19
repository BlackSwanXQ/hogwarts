package com.example.hogwarts.controllers;

import com.example.hogwarts.entity.Faculty;
import com.example.hogwarts.entity.Student;
import com.example.hogwarts.services.AvatarService;
import com.example.hogwarts.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final AvatarService avatarService;

    public StudentController(StudentService studentService
            , AvatarService avatarService) {
        this.studentService = studentService;
        this.avatarService = avatarService;
    }

    @GetMapping("/greet")
    public ResponseEntity greet() {
        return ResponseEntity.ok("Hello, World!");
    }

    @PostMapping
    @Operation(summary = "create Student")
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @GetMapping("/{id}")
    public Student get(@PathVariable long id) {
        return studentService.get(id);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable long id,
                          @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public Student deleteStudent(@PathVariable long id) {
        return studentService.remove(id);
    }

    @GetMapping(value = "/age", params = "age")
    public List<Student> filterByStudentAge(@RequestParam int age) {
        return studentService.filterByStudentAge(age);
    }

    @GetMapping("/all")
    public Collection<Student> getAllFaculty() {
        return studentService.getAllStudents();
    }

    //    @GetMapping(params ={"minAge; maxAge"})
    @GetMapping("/betweenAge")
    public List<Student> filterByRangeAge(@RequestParam int minAge,
                                          @RequestParam int maxAge) {
        return studentService.filterByRangeAge(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public Faculty findStudentFaculty(@PathVariable long id) {
        return studentService.findStudentsFaculty(id);
    }

    @GetMapping("/{id}/avatar-from-db")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable long id) {
        return buildResponseEntity(avatarService.getAvatarFromDb(id));
    }

    @GetMapping("/{id}/avatar-from-fs")
    public ResponseEntity<byte[]> getAvatarFromFs(@PathVariable long id) {
        return buildResponseEntity(avatarService.getAvatarFromFs(id));
    }


    private ResponseEntity<byte[]> buildResponseEntity(Pair<byte[], String> pair) {
        byte[] data = pair.getFirst();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .body(data);

    }

    @GetMapping("/amountStudents")
    public int getAmountStudents() {
        return studentService.getAmountStudents();
    }

    @GetMapping("/averageScore")
    public double getAverageScore() {
        return studentService.getAverageScore();
    }

    @GetMapping("/lastStudents")
    public List<Student> getLastStudents() {
        return studentService.getLastStudents();
    }

    @GetMapping("/startWithA")
    public List<Student> getStudentsStartWithA() {
        return studentService.getStudentsStartWithA();
    }

    @GetMapping("/averageAgeStream")
    public double getAverageAgeStream() {
        return studentService.getAverageAgeStream();
    }

    @GetMapping("/value")
    public int integerValue() {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            sum = IntStream.range(0, 1_000_001)
                    .parallel()
                    .sum();
            System.out.println(System.currentTimeMillis() - start);
        }
        return sum;
    }

}
