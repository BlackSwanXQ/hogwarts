package com.example.hogwarts.repository;

import com.example.hogwarts.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
Optional<Avatar> findStudentById(Long avatarId);
Avatar findAvatarById(Long avatarId);

}
