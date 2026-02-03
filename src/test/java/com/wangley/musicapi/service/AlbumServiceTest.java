package com.wangley.musicapi.service;

import com.wangley.musicapi.domain.entity.Album;
import com.wangley.musicapi.domain.entity.Artist;
import com.wangley.musicapi.domain.enums.TypeArtist;
import com.wangley.musicapi.dto.request.AlbumCreateRequest;
import com.wangley.musicapi.dto.response.AlbumResponse;
import com.wangley.musicapi.exception.ResourceNotFoundException;
import com.wangley.musicapi.repository.AlbumRepository;
import com.wangley.musicapi.repository.ArtistRepository;
import com.wangley.musicapi.websocket.AlbumEventPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do serviço do Álbum
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
class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private AlbumEventPublisher albumEventPublisher;

    @InjectMocks
    private AlbumService albumService;

    @DisplayName("Deve criar álbum com sucesso")
    @Test
    void shouldCreateAlbumSuccessfully() {

        Long artistId = 1L;

        Artist artist = new Artist();
        artist.setId(artistId);
        artist.setNome("Metálica");
        artist.setTipo(TypeArtist.BANDA);

        AlbumCreateRequest albumCreateRequest = new AlbumCreateRequest(
                "Black Album",
                LocalDate.of(1991,8,12),
                Set.of(artistId)
        );

        when(artistRepository.findAllById(albumCreateRequest.artistIds()))
                .thenReturn(List.of(artist));

        Album savedAlbum = new Album();
        savedAlbum.setId(10L);
        savedAlbum.setNome("Black Album");
        savedAlbum.setDataLancamento(albumCreateRequest.dataLancamento());
        savedAlbum.setArtistas(Set.of(artist));

        when(albumRepository.save(any(Album.class)))
                .thenReturn(savedAlbum);

        AlbumResponse albumResponse = albumService.create(albumCreateRequest);

        assertNotNull(albumResponse);
        assertEquals("Black Album", albumResponse.nome());
        assertEquals(1, albumResponse.artistas().size());

        verify(albumRepository).save(any(Album.class));
        verify(albumEventPublisher).publishAlbumCreated(10L, "Black Album");
    }

    @DisplayName("Deve lançar excessão quando artista não for encontrado ao cadastrar o álbum")
    @Test
    void shouldThrowExceptionWhenArtistNotFoundOnCreate() {

        AlbumCreateRequest albumCreateRequest = new AlbumCreateRequest(
                "Album 123",
                LocalDate.now(),
                Set.of(1L, 2L)
        );

        when(artistRepository.findAllById(albumCreateRequest.artistIds()))
                .thenReturn(List.of());

        assertThrows(
                IllegalArgumentException.class,
                () -> albumService.create(albumCreateRequest)
        );

        verify(albumRepository, never()).save(any());
        verify(albumEventPublisher, never()).publishAlbumCreated(any(), any());
    }

    @DisplayName("Deve encontrar álbum por ID com sucesso")
    @Test
    void shouldFindAlbumByIdSuccessfully() {

        Album album = new Album();
        album.setId(1L);
        album.setNome("Hello World!!!");
        album.setDataLancamento(LocalDate.of(2000,10,12));

        when(albumRepository.findById(1L))
                .thenReturn(Optional.of(album));

        AlbumResponse albumResponse = albumService.findById(1L);

        assertEquals("Hello World!!!", albumResponse.nome());
    }

    @DisplayName("Deve lançar excessão quando álbum não for encontrado")
    @Test
    void shouldThrowExceptionWhenAlbumNotFound() {

        when(albumRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> albumService.findById(999L)
        );
    }

    @DisplayName("Deve atualizar com sucesso")
    @Test
    void shouldUpdatedAlbumSuccessfully() {

        Artist artist = new Artist();
        artist.setId(1L);

        Album album = new Album();
        album.setId(1L);

        AlbumCreateRequest albumCreateRequest = new AlbumCreateRequest(
                "Álbum atualizado",
                LocalDate.now(),
                Set.of(1L)
        );

        when(albumRepository.findById(1L))
                .thenReturn(Optional.of(album));

        when(artistRepository.findAllById(albumCreateRequest.artistIds()))
                .thenReturn(List.of(artist));

        when(albumRepository.save(any()))
                .thenReturn(album);

        AlbumResponse albumResponse = albumService.update(1L, albumCreateRequest);

        assertEquals("Álbum atualizado", albumResponse.nome());
    }

    @DisplayName("Deve lançar excessão ao atualizar álbum inexistente")
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingAlbum() {

        when(albumRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> albumService.update(1L, mock(AlbumCreateRequest.class))
        );
    }

    @DisplayName("Deve retornar álbuns paginados com sucesso")
    @Test
    void shouldReturnPagedAlbumsSuccessfully() {

        Album album = new Album();
        album.setId(1L);
        album.setNome("Album testeee");

        Page<Album> albumPage = new PageImpl<>(List.of(album));

        when(albumRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(albumPage);

        Page<AlbumResponse> albumResponsePage = albumService.findAll(
                PageRequest.of(0,10),
                null, null, null, Sort.Direction.ASC
        );

        assertEquals(1, albumResponsePage.getTotalElements());

    }
}