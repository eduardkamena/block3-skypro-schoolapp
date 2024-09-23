package ru.hogwarts.school.service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface AvatarService {

    void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException;

    Avatar findAvatar(Long studentId);

    String getExtension(String fileName);

    byte[] generateImagePreview(Path filePath) throws IOException;

    List<Avatar> findAll(Integer pageNumber, Integer pageSize);

}
