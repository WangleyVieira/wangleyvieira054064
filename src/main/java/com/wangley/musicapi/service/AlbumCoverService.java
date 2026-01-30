package com.wangley.musicapi.service;

import com.wangley.musicapi.domain.entity.Album;
import com.wangley.musicapi.domain.entity.AlbumCover;
import com.wangley.musicapi.exception.ResourceNotFoundException;
import com.wangley.musicapi.repository.AlbumCoverRepository;
import com.wangley.musicapi.repository.AlbumRepository;
import com.wangley.musicapi.service.infra.MinioService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@Service
public class AlbumCoverService {

    private final AlbumRepository albumRepository;
    private final AlbumCoverRepository albumCoverRepository;
    private final MinioService minioService;

    public AlbumCoverService(
            AlbumRepository albumRepository,
            AlbumCoverRepository albumCoverRepository,
            MinioService minioService
    ) {
        this.albumRepository = albumRepository;
        this.albumCoverRepository = albumCoverRepository;
        this.minioService = minioService;
    }

    @Transactional
    public void uploadCover(Long albumId, MultipartFile file) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Album não encontrado"));

        String objectName =
                "albums/" + albumId + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        minioService.upload("album-covers", objectName, file);

        AlbumCover cover = new AlbumCover(objectName, album);
        albumCoverRepository.save(cover);

    }
}


