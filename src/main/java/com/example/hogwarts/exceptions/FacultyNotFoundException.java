package com.example.hogwarts.exceptions;

public class FacultyNotFoundException extends NotFoundException {
    public FacultyNotFoundException(long id) {
        super(id);
    }

    @Override
    public String getMessage() {
        return "Faculty with id = %d not found".formatted(getId());
    }




}
