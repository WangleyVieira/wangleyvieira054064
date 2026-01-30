package com.wangley.musicapi.utils.storage.validation;
import com.wangley.musicapi.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class ImageFileValidator {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/png",
            "image/jpeg"
    );

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "png", "jpg", "jpeg"
    );

    public static void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new BusinessException("Arquivo não pode ser vazio");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new BusinessException("Tipo de arquivo não permitido. Envie PNG, JPG ou JPEG");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !hasValidExtension(originalFilename)) {
            throw new BusinessException(
                    "Extensão de arquivo inválida. Permitido: png, jpg, jpeg"
            );
        }
    }

    private static boolean hasValidExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex < 0) {
            return false;
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(extension);
    }
}

