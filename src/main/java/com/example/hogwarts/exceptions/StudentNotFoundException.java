package com.example.hogwarts.exceptions;

public class StudentNotFoundException extends NotFoundException{

    public StudentNotFoundException(long id) {
        super(id);
    }



    @Override
    public String getMessage() {
        return "Student with id = %d not found".formatted(getId());
    }
}
