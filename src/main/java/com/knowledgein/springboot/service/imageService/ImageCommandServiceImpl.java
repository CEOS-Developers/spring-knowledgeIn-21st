package com.knowledgein.springboot.service.imageService;

import com.knowledgein.springboot.apiPayload.code.status.ErrorStatus;
import com.knowledgein.springboot.apiPayload.exception.GeneralException;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageCommandServiceImpl implements ImageCommandService {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Override
    public String uploadFile(MultipartFile image, String extension) {
        String originalFilename = image.getOriginalFilename();
        String key = UUID.randomUUID() + "_" + (originalFilename != null ? originalFilename : "image." + extension);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(image.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(image.getInputStream(), image.getSize()));

            // 업로드한 파일의 URL 반환
            return s3Client.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build()).toString();

        } catch (IOException e) {
            throw new GeneralException(ErrorStatus.CANNOT_UPLOAD_S3);
        }

    }
}
