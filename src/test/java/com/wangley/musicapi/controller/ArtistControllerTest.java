package com.wangley.musicapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangley.musicapi.dto.request.ArtistCreateRequest;
import com.wangley.musicapi.domain.enums.TypeArtist;
import com.wangley.musicapi.domain.entity.Artist;
import com.wangley.musicapi.dto.request.ArtistUpdateRequest;
import com.wangley.musicapi.dto.response.AlbumSimpleResponse;
import com.wangley.musicapi.dto.response.ArtistResponse;
import com.wangley.musicapi.exception.ResourceNotFoundException;
import com.wangley.musicapi.infrastructure.ratelimit.RateLimitFilter;
import com.wangley.musicapi.security.JwtAuthenticationFilter;
import com.wangley.musicapi.security.JwtTokenProvider;
import com.wangley.musicapi.service.ArtistService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = ArtistController.class)
@AutoConfigureMockMvc(addFilters = false)
class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistService artistService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private RateLimitFilter rateLimitFilter;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("Deve cadastrar artista com sucesso")
    void shouldCreateArtistSuccessfully() throws Exception {

        ArtistCreateRequest request =
                new ArtistCreateRequest("Teste 1", TypeArtist.BANDA);

        Artist artist = new Artist();
        artist.setId(1L);
        artist.setNome("Teste 1");
        artist.setTipo(TypeArtist.BANDA);

        when(artistService.create(any(ArtistCreateRequest.class)))
                .thenReturn(artist);

        mockMvc.perform(post("/v1/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Teste 1"))
                .andExpect(jsonPath("$.tipo").value("BANDA"));

        verify(artistService).create(any(ArtistCreateRequest.class));
    }

    @DisplayName("Deve retornar 400 quando payload inválido")
    @Test
    void shouldReturn400WhenCreateArtistWithInvalidData() throws Exception {

        ArtistCreateRequest request =
                new ArtistCreateRequest("", null);

        mockMvc.perform(post("/v1/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(artistService);
    }

    @Test
    @DisplayName("Deve atualizar um artista com sucesso")
    void shouldUpdateArtistSuccessfully() throws Exception {

        ArtistUpdateRequest request =
                new ArtistUpdateRequest("Nirvana", TypeArtist.BANDA);

        ArtistResponse response =
                new ArtistResponse(
                        1L,
                        "Nirvana",
                        TypeArtist.BANDA,
                        List.of(
                                new AlbumSimpleResponse(10L, "Nevermind")
                        )
                );

        when(artistService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/v1/artists/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nirvana"))
                .andExpect(jsonPath("$.albums").isArray())
                .andExpect(jsonPath("$.albums[0].nome").value("Nevermind"));

        verify(artistService).update(eq(1L), any());
    }


    @DisplayName("Deve retornar 404 quando artista não existir")
    @Test
    void shouldReturn404WhenUpdateArtistNotFound() throws Exception {

        when(artistService.update(eq(99L), any()))
                .thenThrow(new ResourceNotFoundException("Artista não encontrado"));

        ArtistUpdateRequest request =
                new ArtistUpdateRequest("Teste", TypeArtist.CANTOR);

        mockMvc.perform(put("/v1/artists/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve listar artistas com seus álbuns")
    void shouldListArtistsSuccessfully() throws Exception {

        List<ArtistResponse> artists = List.of(
                new ArtistResponse(
                        1L,
                        "Teste 2",
                        TypeArtist.BANDA,
                        List.of(
                                new AlbumSimpleResponse(20L, "Album Teste")
                        )
                )
        );

        when(artistService.list()).thenReturn(artists);

        mockMvc.perform(get("/v1/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Teste 2"))
                .andExpect(jsonPath("$[0].albums").isArray())
                .andExpect(jsonPath("$[0].albums[0].nome").value("Album Teste"));

        verify(artistService).list();
    }

    @DisplayName("Deve retornar 404 quando não encontrado")
    @Test
    void shouldReturn404WhenFindArtistByIdNotFound() throws Exception {

        when(artistService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Artista não encontrado"));

        mockMvc.perform(get("/v1/artists/{id}", 99L))
                .andExpect(status().isNotFound());
    }

}