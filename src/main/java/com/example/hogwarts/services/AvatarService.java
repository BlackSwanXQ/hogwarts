package com.example.hogwarts.services;


import com.example.hogwarts.entity.Avatar;
import com.example.hogwarts.entity.Student;
import com.example.hogwarts.exceptions.StudentNotFoundException;
import com.example.hogwarts.repository.AvatarRepository;
import com.example.hogwarts.repository.StudentRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
//@Transactional
public class AvatarService {
    private final AvatarRepository avatarRepository;
//    private final StudentService studentService;
    private final StudentRepository studentRepository;
    ;

    @Value("${path.to.avatars.folder}")
    private String avatarDir;

    public AvatarService(AvatarRepository avatarRepository,
                         StudentRepository studentRepository,
                         StudentService studentService) {
        this.avatarRepository = avatarRepository;
//        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    public void uploadAvatar(Long id, MultipartFile file) throws IOException {
        Student student = studentRepository.findById(id)
                        .orElseThrow(() -> new StudentNotFoundException(id));
        Path filePath = Path.of(avatarDir, student + getExtension(file.getOriginalFilename()));

        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = findAvatar(id);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
//        avatar.setId(id);

        avatarRepository.save(avatar);

    }


    public ResponseEntity<byte[]> downloadAvatarFromDataBase(Long id) throws IOException {
        Avatar avatar = findAvatar(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        httpHeaders.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(avatar.getData());
    }

    public void downloadAvatarFromDir(Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarRepository.findAvatarById(id);
        Path path = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();
        ) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength(avatar.getData().length);
            is.transferTo(os);
        }

    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }

    public Avatar findAvatar(Long id) {
        return avatarRepository.findStudentById(id).orElse(new Avatar());
    }

}
