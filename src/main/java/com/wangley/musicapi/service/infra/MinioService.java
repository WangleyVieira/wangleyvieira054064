package com.wangley.musicapi.service.infra;

import com.wangley.musicapi.exception.StorageException;
import com.wangley.musicapi.utils.storage.validation.ImageFileValidator;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class MinioService {

    private final MinioClient minioClient;

    public void upload(String bucket, String objectName, MultipartFile file) {

        ImageFileValidator.validate(file);

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(
                                    file.getInputStream(),
                                    file.getSize(),
                                    -1
                            )
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new StorageException("Erro ao enviar arquivo para o MinIO", e);
        }
    }
}
