package com.ceos21.knowledgein.post.config;

import com.ceos21.knowledgein.post.domain.Image;
import com.ceos21.knowledgein.post.exception.PostException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ceos21.knowledgein.post.exception.PostErrorCode.SAVE_IMAGE_FAILED;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public List<Image> storeFiles(List<MultipartFile> files){
        List<Image> storeFileResult = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                storeFileResult.add(storeFile(file));
            }
        }
        return storeFileResult;
    }

    public Image storeFile(MultipartFile file){
        if (file.isEmpty()) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        try {
            file.transferTo(new File(getFullPath(storeFileName)));
        } catch (IOException e) {
            throw new PostException(SAVE_IMAGE_FAILED);
        }
        return Image.createWithNoPost(storeFileName, originalFilename);
    }

    private String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    private String createStoreFileName(String originalFilename) {
        String extension = extractExtension(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + extension;
    }

    private String extractExtension(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos+1);
    }

}