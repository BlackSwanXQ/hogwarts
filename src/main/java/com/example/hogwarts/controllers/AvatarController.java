package com.example.hogwarts.controllers;

import com.example.hogwarts.entity.Avatar;
import com.example.hogwarts.services.AvatarService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/avatar")


public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{id}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadAvatar(@PathVariable Long id,
                             @RequestParam MultipartFile multipartFile) {
        avatarService.uploadAvatar(multipartFile, id);
    }

//    @GetMapping("/{id}/database")
//    public ResponseEntity<byte[]> downloadAvatarFromDataBase(@PathVariable Long id) throws IOException {
////        return avatarService.downloadAvatarFromDataBase(id);
//        return null;
//    }
//
//    @GetMapping("/{id}/dir")
//    public void downloadAvatarFromDir(@PathVariable Long id, HttpServletResponse response) throws IOException {
////        avatarService.downloadAvatarFromDir(id, response);
//    }


}
