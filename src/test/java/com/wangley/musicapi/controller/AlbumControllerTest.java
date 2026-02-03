package com.wangley.musicapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangley.musicapi.domain.enums.TypeArtist;
import com.wangley.musicapi.dto.request.AlbumCreateRequest;
import com.wangley.musicapi.dto.response.AlbumCoverUrlResponse;
import com.wangley.musicapi.dto.response.AlbumResponse;
import com.wangley.musicapi.dto.response.ArtistResumeResponse;
import com.wangley.musicapi.infrastructure.ratelimit.RateLimitFilter;
import com.wangley.musicapi.security.JwtAuthenticationFilter;
import com.wangley.musicapi.security.JwtTokenProvider;
import com.wangley.musicapi.service.AlbumService;
import com.wangley.musicapi.service.AlbumCoverService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = AlbumController.class)
@AutoConfigureMockMvc(addFilters = false)
class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlbumService albumService;

    @MockBean
    private AlbumCoverService albumCoverService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private RateLimitFilter rateLimitFilter;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("Deve cadastrar um álbum com sucesso")
    void shouldCreateAlbumSuccessfully() throws Exception{

        AlbumCreateRequest request = new AlbumCreateRequest(
                "Teste de álbum",
                LocalDate.of(1991, 8, 12),
                Set.of(1L)
        );

        AlbumResponse response = new AlbumResponse(
                10L,
                "Teste de álbum",
                LocalDate.of(2000, 8, 12),
                Set.of(
                        new ArtistResumeResponse(
                                1L,
                                "Teste 1",
                                TypeArtist.BANDA
                        )
                )
        );

        when(albumService.create(any(AlbumCreateRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/v1/albums")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nome").value("Teste de álbum"));

        verify(albumService).create(any(AlbumCreateRequest.class));
    }

    @Test
    @DisplayName("Deve buscar álbum por ID com sucesso")
    void shouldFindAlbumByIdSuccessfully() throws Exception {

        AlbumResponse response = new AlbumResponse(
                10L,
                "Teste álbum",
                LocalDate.of(1991, 8, 12),
                Set.of()
        );

        when(albumService.findById(10L)).thenReturn(response);

        mockMvc.perform(get("/v1/albums/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nome").value("Teste álbum"));

        verify(albumService).findById(10L);
    }

    @Test
    @DisplayName("Deve atualizar álbum com sucesso")
    void shouldUpdateAlbumSuccessfully() throws Exception {

        AlbumCreateRequest request = new AlbumCreateRequest(
                "Álbum atualizado",
                LocalDate.of(1995, 1, 1),
                Set.of(1L)
        );

        AlbumResponse response = new AlbumResponse(
                10L,
                "Álbum atualizado",
                LocalDate.of(1995, 1, 1),
                Set.of()
        );

        when(albumService.update(eq(10L), any(AlbumCreateRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        put("/v1/albums/{id}", 10L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Álbum atualizado"));

        verify(albumService).update(eq(10L), any());
    }


    @Test
    @DisplayName("Deve listar álbuns com paginação")
    void shouldListAlbumsSuccessfully() throws Exception {

        Page<AlbumResponse> page = new PageImpl<>(List.of(
                new AlbumResponse(
                        1L,
                        "Album 1",
                        LocalDate.now(),
                        Set.of()
                )
        ));

        when(albumService.findAll(
                any(Pageable.class),
                any(),
                any(),
                any(),
                any()
        )).thenReturn(page);

        mockMvc.perform(get("/v1/albums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].nome").value("Album 1"));
    }

    @Test
    @DisplayName("Deve fazer upload da capa do álbum")
    void shouldUploadAlbumCoverSuccessfully() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "cover.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image".getBytes()
        );

        mockMvc.perform(
                        multipart("/v1/albums/{id}/cover", 10L)
                                .file(file)
                )
                .andExpect(status().isNoContent());

        verify(albumCoverService).uploadCover(eq(10L), any());
    }

    @Test
    @DisplayName("Deve gerar URL temporária da capa do álbum")
    void shouldGenerateCoverUrlSuccessfully() throws Exception {

        AlbumCoverUrlResponse response =
                new AlbumCoverUrlResponse(
                        "http://example.com/cover",
                        LocalDateTime.now().plusMinutes(30)
                );

        when(albumCoverService.generateCoverUrl(10L))
                .thenReturn(response);

        mockMvc.perform(get("/v1/albums/{id}/cover/url", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("http://example.com/cover"))
                .andExpect(jsonPath("$.expiresAt").exists());

        verify(albumCoverService).generateCoverUrl(10L);
    }

}