package com.wangley.musicapi.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import com.wangley.musicapi.domain.enums.TypeArtist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArtistTest {

    @Test
    @DisplayName("Deve inicializar a lista de álbuns como um HashSet vazio ao instanciar")
    void shouldInitializeAlbumsList() {

        Artist artist = new Artist();

        assertNotNull(artist.getAlbums(), "A lista de álbuns deve ser inicializada para evitar NullPointerException");
        assertTrue(artist.getAlbums().isEmpty());
    }

    @Test
    @DisplayName("Deve atribuir nome e tipo corretamente")
    void shouldSetArtistDetails() {

        Artist artist = new Artist();
        String nomeEsperado = "Serj Tankian"; // Usando dados do desafio
        TypeArtist tipoEsperado = TypeArtist.CANTOR;

        artist.setNome(nomeEsperado);
        artist.setTipo(tipoEsperado);

        assertEquals(nomeEsperado, artist.getNome());
        assertEquals(tipoEsperado, artist.getTipo());
    }

    @Test
    @DisplayName("Deve permitir adicionar álbuns ao relacionamento ManyToMany")
    void shouldManageAlbumRelationship() {

        Artist artist = new Artist();
        Album album = new Album();
        album.setNome("Harakiri");

        artist.getAlbums().add(album);

        assertEquals(1, artist.getAlbums().size());
        assertTrue(artist.getAlbums().contains(album));
    }
}