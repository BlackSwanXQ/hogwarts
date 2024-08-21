package com.example.hogwarts.services;

import com.example.hogwarts.entity.Faculty;
import com.example.hogwarts.exceptions.FacultyNotFoundException;
import com.example.hogwarts.exceptions.StudentNotFoundException;
import com.example.hogwarts.entity.Student;
import com.example.hogwarts.repository.FacultyRepository;
import com.example.hogwarts.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {


    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);
    public int count = 0;

    public Student create(Student student) {
        logger.info("Was invoked method for create Student with name: " + student.getName());
        Faculty faculty = null;
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> {
                        logger.error("There is not such Faculty");
                        return new FacultyNotFoundException(student.getFaculty().getId());
                    });
        }
        student.setFaculty(faculty);
        student.setId(null);
        logger.debug("Student with name {} was created", student.getName());
        return studentRepository.save(student);
    }

    public Student get(long id) {
        logger.info("Was invoked method for get Student with id = " + id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not Student with id = " + id);
                    return new StudentNotFoundException(id);
                });
    }


    public Student update(long id, Student student) {
        logger.info("Was invoked method for update Student with name: " + student.getName());
        Student oldStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        Faculty faculty = null;
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> new FacultyNotFoundException(student.getFaculty().getId()));
        }
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        oldStudent.setFaculty(faculty);
        logger.debug("Student with name {} was updated", student.getName());
        return studentRepository.save(oldStudent);
    }

    public Student remove(long id) {
        logger.info("Was invoked method for remove Student with id: " + id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not Student with id = " + id);
                    return new StudentNotFoundException(id);
                });
        studentRepository.delete(student);
        logger.debug("Student with id {} was removed", student.getId());
        return student;
    }

    public List<Student> filterByStudentAge(int age) {
        logger.info("Was invoked method for filterByStudentAge: " + age);
        return studentRepository.findAllByAge(age);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> filterByRangeAge(int minAge, int maxAge) {
        logger.info("Was invoked method for filterByRange from {} to " + minAge, maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }


    public Faculty findStudentsFaculty(long id) {
        logger.info("Was invoked method for findStudentsFaculty: " + id);
        return get(id).getFaculty();
    }


    public int getAmountStudents() {
        logger.info("Was invoked method for getAmountStudents");
        return studentRepository.getAmountStudents();
    }

    public double getAverageScore() {
        logger.info("Was invoked method for getAverageScore");
        return studentRepository.getAverageScore();
    }

    public List<Student> getLastStudents() {
        logger.info("Was invoked method for getLastStudents");
        return studentRepository.getLastStudents();
    }

    public List<Student> getStudentsStartWithA() {
        return getAllStudents()
                .stream()
                .filter(s -> s.getName().startsWith("A"))
                .toList();
    }

    public double getAverageAgeStream() {
        return getAllStudents()
                .stream()
                .mapToDouble(Student::getAge).average()
                .orElseThrow();
    }

    public void printStudentsParallel() {
        printParallel(getAllStudents().get(0).getName());
        printParallel(getAllStudents().get(1).getName());
        new Thread(() -> {
            printParallel(getAllStudents().get(2).getName());
            printParallel(getAllStudents().get(3).getName());
        }).start();
        new Thread(() -> {
            printParallel(getAllStudents().get(4).getName());
            printParallel(getAllStudents().get(5).getName());
        }).start();
    }

    private void printParallel(String student) {
        System.out.println(Thread.currentThread().getName() + ": " + student);
    }

    public void printStudentsSynchronized() {
        printSynchronized(getAllStudents().get(0).getName());
        printSynchronized(getAllStudents().get(1).getName());
        new Thread(() -> {
            printSynchronized(getAllStudents().get(2).getName());
            printSynchronized(getAllStudents().get(3).getName());
        }).start();
        new Thread(() -> {
            printSynchronized(getAllStudents().get(4).getName());
            printSynchronized(getAllStudents().get(5).getName());
        }).start();
    }

    private void printSynchronized(String student) {
        synchronized (StudentService.class) {
            System.out.println(Thread.currentThread().getName() + ": " + student + " - Counter - " + count);
        }
        count++;
    }
}
