package com.wangley.musicapi.service;

import com.wangley.musicapi.domain.entity.Artist;
import com.wangley.musicapi.domain.enums.TypeArtist;
import com.wangley.musicapi.dto.request.ArtistCreateRequest;
import com.wangley.musicapi.dto.request.ArtistUpdateRequest;
import com.wangley.musicapi.dto.response.ArtistResponse;
import com.wangley.musicapi.exception.ResourceNotFoundException;
import com.wangley.musicapi.repository.ArtistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do serviço de Artista
 *
 * Objetivo:
 * - Validar regras de negócio
 * - Garantir comportamento correto do serviço
 * - Isolar dependências externas usando mocks
 *
 * OBS.:
 * - Nenhum acesso real ao Banco de Dados
 * - Repository e dependências são simulados com Mockito
 */
@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistService artistService;

    @DisplayName("Deve criar artista com sucesso")
    @Test
    void shouldCreateArtistSuccessfully() {

        ArtistCreateRequest artistCreateRequest = new ArtistCreateRequest("Metálica", TypeArtist.BANDA);

        Artist savedArtist = new Artist();
        savedArtist.setId(1L);
        savedArtist.setNome("Metálica");
        savedArtist.setTipo(TypeArtist.BANDA);

        when(artistRepository.save(any(Artist.class))).thenReturn(savedArtist);

        Artist result = artistService.create(artistCreateRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Metálica", result.getNome());
        assertEquals(TypeArtist.BANDA, result.getTipo());

        verify(artistRepository).save(any(Artist.class));
    }

    @DisplayName("Deve lançar excessão ao atualizar artista inexistente")
    @Test
    void shouldThrowExceptionOnUpdateArtistNonexisting() {

        Long artistId = 99L;

        ArtistUpdateRequest artistUpdateRequest = new ArtistUpdateRequest("Novo nome", TypeArtist.CANTOR);

        when((artistRepository.findById(artistId))).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> artistService.update(artistId, artistUpdateRequest)
        );
        verify(artistRepository, never()).save(any());
    }

    @DisplayName("Deve atualizar artista com sucesso")
    @Test
    void shouldUpdateArtistSuccessfully() {

        Long artistId = 1L;

        ArtistUpdateRequest artistUpdateRequest =
                new ArtistUpdateRequest("Nirvana", TypeArtist.BANDA);

        Artist existingArtist = new Artist();
        existingArtist.setId(artistId);
        existingArtist.setNome("Metálica");
        existingArtist.setTipo(TypeArtist.BANDA);

        Artist updatedArtist = new Artist();
        updatedArtist.setId(artistId);
        updatedArtist.setNome("Nirvana");
        updatedArtist.setTipo(TypeArtist.BANDA);

        when(artistRepository.findById(artistId))
                .thenReturn(Optional.of(existingArtist));

        when(artistRepository.save(any(Artist.class)))
                .thenReturn(updatedArtist);

        ArtistResponse result =
                artistService.update(artistId, artistUpdateRequest);

        assertNotNull(result);
        assertEquals(artistId, result.id());
        assertEquals("Nirvana", result.nome());
        assertEquals(TypeArtist.BANDA, result.tipo());
        assertNotNull(result.albums());

        verify(artistRepository).findById(artistId);
        verify(artistRepository).save(existingArtist);
    }

    @DisplayName("Deve lançar excessão quando artista não for encontrado")
    @Test
    void shouldThrowExceptionWhenArtistNotFound() {

        Long artistId = 99L;

        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> artistService.findById(artistId)
        );

        verify(artistRepository).findById(artistId);
    }

    @DisplayName("Deve encontrar artista por ID com sucesso")
    @Test
    void shouldFindArtistByIdSuccessfully() {

        Long artistId = 1L;

        Artist artist = new Artist();
        artist.setId(artistId);
        artist.setNome("Metálica");
        artist.setTipo(TypeArtist.BANDA);

        when(artistRepository.findById(artistId))
                .thenReturn(Optional.of(artist));

        ArtistResponse result = artistService.findById(artistId);

        assertNotNull(result);
        assertEquals(artistId, result.id());
        assertEquals("Metálica", result.nome());
        assertEquals(TypeArtist.BANDA, result.tipo());
        assertNotNull(result.albums());

        verify(artistRepository).findById(artistId);
    }

    @DisplayName("Deve listar artistas com sucesso")
    @Test
    void shouldListArtistsSuccessfully() {

        Artist artist1 = new Artist();
        artist1.setId(1L);
        artist1.setNome("Metálica");
        artist1.setTipo(TypeArtist.BANDA);

        Artist artist2 = new Artist();
        artist2.setId(2L);
        artist2.setNome("Nirvana");
        artist2.setTipo(TypeArtist.BANDA);

        when(artistRepository.findAll()).thenReturn(List.of(artist1, artist2));

        List<ArtistResponse> result = artistService.list();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Metálica", result.get(0).nome());
        assertEquals(TypeArtist.BANDA, result.get(0).tipo());

        assertEquals("Nirvana", result.get(1).nome());
        assertEquals(TypeArtist.BANDA, result.get(1).tipo());

        verify(artistRepository).findAll();
    }

    @DisplayName("Deve retornar lista vazia quando nenhum artista não for encontrado")
    @Test
    void shouldReturnEmptyListWhenNoArtistsFound() {
        when(artistRepository.findAll()).thenReturn(Collections.emptyList());

        List<ArtistResponse> result = artistService.list();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(artistRepository).findAll();
    }

}