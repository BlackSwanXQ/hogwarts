package com.example.hogwarts.dto;

import com.example.hogwarts.entity.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Setter
@Getter
public class AvatarDto {

    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    private String data;
    private Student student;

    public AvatarDto(Long id, String filePath, long fileSize, String mediaType, String data, Student student) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
        this.student = student;
    }
}
