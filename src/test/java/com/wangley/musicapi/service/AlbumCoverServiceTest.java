package com.wangley.musicapi.service;

import com.wangley.musicapi.domain.entity.Album;
import com.wangley.musicapi.domain.entity.AlbumCover;
import com.wangley.musicapi.dto.response.AlbumCoverUrlResponse;
import com.wangley.musicapi.exception.ResourceNotFoundException;
import com.wangley.musicapi.repository.AlbumCoverRepository;
import com.wangley.musicapi.repository.AlbumRepository;
import com.wangley.musicapi.service.infra.MinioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumCoverServiceTest {

    @InjectMocks
    private AlbumCoverService albumCoverService;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private AlbumCoverRepository albumCoverRepository;

    @Mock
    private MinioService minioService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(albumCoverService, "bucket", "album-covers");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar gerar URL quando álbum não existe")
    void shouldThrowExceptionWhenAlbumNotFound() {

        when(albumRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> albumCoverService.generateCoverUrl(1L)
        );

        verify(albumRepository).findById(1L);
        verifyNoMoreInteractions(albumCoverRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando álbum não possui capa")
    void shouldThrowExceptionWhenCoverNotFound() {

        Album album = new Album();
        album.setId(1L);

        when(albumRepository.findById(1L))
                .thenReturn(Optional.of(album));

        when(albumCoverRepository.findByAlbumId(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> albumCoverService.generateCoverUrl(1L)
        );

        verify(albumCoverRepository).findByAlbumId(1L);
    }

    @Test
    @DisplayName("Deve gerar URL pré-assinada da capa com sucesso")
    void shouldGenerateCoverUrlSuccessfully() {

        Album album = new Album();
        album.setId(1L);

        AlbumCover cover = new AlbumCover(
                "albums/1/capa.jpg",
                album
        );

        when(albumRepository.findById(1L))
                .thenReturn(Optional.of(album));

        when(albumCoverRepository.findByAlbumId(1L))
                .thenReturn(Optional.of(cover));

        when(minioService.generatePresignedGetUrl(
                anyString(),
                anyString(),
                anyInt()
        )).thenReturn("http://minio-url");

        AlbumCoverUrlResponse response =
                albumCoverService.generateCoverUrl(1L);

        assertNotNull(response);
        assertEquals("http://minio-url", response.url());
        assertNotNull(response.expiresAt());

        verify(minioService).generatePresignedGetUrl(
                eq("album-covers"),
                eq("albums/1/capa.jpg"),
                eq(30)
        );
    }
}