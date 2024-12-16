package com.example.hogwarts.services;


import com.example.hogwarts.entity.Avatar;
import com.example.hogwarts.entity.Student;
import com.example.hogwarts.exceptions.AvatarProcessingException;
import com.example.hogwarts.exceptions.StudentNotFoundException;
import com.example.hogwarts.repository.AvatarRepository;
import com.example.hogwarts.repository.StudentRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import static java.nio.file.Paths.get;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
//@Transactional
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private final Path path;

    public AvatarService(AvatarRepository avatarRepository,
                         StudentRepository studentRepository,
                         @Value("${path.to.avatars.folder}") String avatarDirName) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        path = get(avatarDirName);
    }

    @Transactional
    public void uploadAvatar(MultipartFile multipartFile, Long id) {
        try {
            byte[] data = multipartFile.getBytes();
            String extention = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
            Path avatarPath = path.resolve(UUID.randomUUID().toString() + "." + extention);
            Files.write(avatarPath, data);
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new StudentNotFoundException(id));
            Avatar avatar = avatarRepository.findByStudent_Id(id)
                    .orElseGet(Avatar::new);
            avatar.setStudent(student);
            avatar.setData(data);
            avatar.setFileSize(data.length);
            avatar.setMediaType(multipartFile.getContentType());
            avatar.setFilePath(avatarPath.toString());
            avatarRepository.save(avatar);
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }
    }


    public Pair<byte[], String> getAvatarFromDb(long id) {
        Avatar avatar = avatarRepository.findByStudent_Id(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }


    public Pair<byte[], String> getAvatarFromFs(long id) {
        try {
            Avatar avatar = avatarRepository.findByStudent_Id(id)
                    .orElseThrow(() -> new StudentNotFoundException(id));
            return Pair.of(Files.readAllBytes(Paths.get(avatar.getFilePath())),avatar.getMediaType());
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }
    }

}
