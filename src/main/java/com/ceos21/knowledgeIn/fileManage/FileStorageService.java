package com.ceos21.knowledgeIn.fileManage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String storeFile(MultipartFile file) throws IOException;

    public void deleteFile(String filePath) throws IOException;

}
