package com.wangley.musicapi.domain.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

class AlbumTest {

    @Test
    @DisplayName("Deve inicializar coleções vazias ao criar um novo álbum")
    void shouldInitializeCollections() {

        Album album = new Album();

        assertNotNull(album.getArtistas(), "A coleção de artistas deve ser inicializada para evitar NPE");
        assertNotNull(album.getCovers(), "A coleção de capas deve ser inicializada para evitar NPE");
        assertTrue(album.getArtistas().isEmpty());
        assertTrue(album.getCovers().isEmpty());
    }

    @Test
    @DisplayName("Deve atribuir os dados básicos corretamente")
    void shouldSetBasicData() {

        Album album = new Album();
        String nomeEsperado = "Use Your Illusion I";
        LocalDate dataEsperada = LocalDate.of(1991, 9, 17);

        album.setNome(nomeEsperado);
        album.setDataLancamento(dataEsperada);

        assertEquals(nomeEsperado, album.getNome());
        assertEquals(dataEsperada, album.getDataLancamento());
    }

    @Test
    @DisplayName("Deve gerenciar o relacionamento ManyToMany com Artistas")
    void shouldManageArtistRelationship() {

        Album album = new Album();
        Artist artist = new Artist();
        artist.setNome("Guns N' Roses");

        album.getArtistas().add(artist);

        assertEquals(1, album.getArtistas().size());
        assertTrue(album.getArtistas().contains(artist));
    }
}