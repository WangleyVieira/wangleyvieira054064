package com.wangley.musicapi.service.infra;

import com.wangley.musicapi.exception.StorageException;
import com.wangley.musicapi.infrastructure.minio.MinioProperties;
import com.wangley.musicapi.utils.storage.validation.ImageFileValidator;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

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

    public String generatePresignedGetUrl(
            String bucket,
            String objectName,
            int expirationMinutes
    ) {
        try {
            String internalUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(objectName)
                            .expiry(expirationMinutes, TimeUnit.MINUTES)
                            .build()
            );

            // 🔥 substitui minio → localhost
            return internalUrl.replace(
                    minioProperties.getUrl(),
                    minioProperties.getPublicUrl()
            );

        } catch (Exception e) {
            throw new StorageException("Erro ao gerar URL de download", e);
        }
    }


}
