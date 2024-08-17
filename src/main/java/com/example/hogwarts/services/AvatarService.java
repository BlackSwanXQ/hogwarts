package com.example.hogwarts.services;


import com.example.hogwarts.dto.AvatarDto;
import com.example.hogwarts.entity.Avatar;
import com.example.hogwarts.entity.Student;
import com.example.hogwarts.exceptions.AvatarProcessingException;
import com.example.hogwarts.exceptions.StudentNotFoundException;
import com.example.hogwarts.repository.AvatarRepository;
import com.example.hogwarts.repository.StudentRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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

    private final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Transactional
    public void uploadAvatar(MultipartFile multipartFile, Long id) {
        logger.info("Was invoked method for upload Avatar for Student with id: " + id);
        try {
            byte[] data = multipartFile.getBytes();
            String extention = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
            Path avatarPath = path.resolve(UUID.randomUUID().toString() + "." + extention);
            Files.write(avatarPath, data);
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.error("There is not Student with id = {}", id);
                        return new StudentNotFoundException(id);
                    });
            Avatar avatar = avatarRepository.findByStudent_Id(id)
                    .orElseGet(Avatar::new);
            avatar.setStudent(student);
            avatar.setData(data);
            avatar.setFileSize(data.length);
            avatar.setMediaType(multipartFile.getContentType());
            avatar.setFilePath(avatarPath.toString());
            avatarRepository.save(avatar);
            logger.debug("Avatar for Student with id {} was uploaded", id);
        } catch (IOException e) {
            logger.error("Something went wrong: ", e);
            throw new AvatarProcessingException();
        }
    }


    public Pair<byte[], String> getAvatarFromDb(long id) {
        logger.info("Was invoked method for getAvatarFromDB with id: {}", id);
        Avatar avatar = avatarRepository.findByStudent_Id(id)
                .orElseThrow(() -> {
                    logger.error("There is not Student with id = {}", id);
                    return new StudentNotFoundException(id);
                });
        logger.debug("Avatar for Student with id {} was get", id);
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }


    public Pair<byte[], String> getAvatarFromFs(long id) {
        logger.info("Was invoked method for getAvatarFromFs with id: {}", id);
        try {
            Avatar avatar = avatarRepository.findByStudent_Id(id)
                    .orElseThrow(() -> {
                        logger.error("There is not Student with id = {}", id);
                        return new StudentNotFoundException(id);
                    });
            logger.debug("Avatar for Student with id {} was get", id);
            return Pair.of(Files.readAllBytes(Paths.get(avatar.getFilePath())), avatar.getMediaType());
        } catch (IOException e) {
            logger.error("Something went wrong: ", e);
            throw new AvatarProcessingException();
        }
    }

    public List<AvatarDto> getAvatars(Integer pageNumber, Integer pageSize) {
        logger.info("Was invoked method for getAvatars");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        List<Avatar> avatars = avatarRepository.findAll(pageRequest).getContent();
        logger.debug("Avatars found: {}", avatars.size());
        return avatars
                .stream().map(avatar -> new AvatarDto(
                        avatar.getId(),
                        avatar.getFilePath(),
                        avatar.getFileSize(),
                        avatar.getMediaType(),
                        "localhost:8080/student/%d/avatar-from-db".formatted(avatar.getStudent().getId()),
                        avatar.getStudent())
                ).toList();

    }

}
