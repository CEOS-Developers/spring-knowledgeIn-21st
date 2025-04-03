package com.ceos21.knowledgeIn.fileManage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService{
    private final Path uploadPath = Paths.get("uploads");

    //로컬 파일 저장소 없다면 생성
    public LocalFileStorageService() throws IOException {
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        //파일명 생성
        String fileName = file.getOriginalFilename() + "_" + UUID.randomUUID().toString();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        //local 경로 반환
        return filePath.toAbsolutePath().toString();
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }
}
