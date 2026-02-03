package com.wangley.musicapi.service;

import com.wangley.musicapi.domain.entity.Artist;
import com.wangley.musicapi.dto.request.ArtistCreateRequest;
import com.wangley.musicapi.dto.request.ArtistUpdateRequest;
import com.wangley.musicapi.dto.response.AlbumSimpleResponse;
import com.wangley.musicapi.dto.response.ArtistResponse;
import com.wangley.musicapi.exception.ResourceNotFoundException;
import com.wangley.musicapi.repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistaRepository) {
        this.artistRepository = artistaRepository;
    }

    @Transactional
    public Artist create(ArtistCreateRequest artistaCreateRequest) {
        Artist artista = new Artist();
        artista.setNome(artistaCreateRequest.nome());
        artista.setTipo(artistaCreateRequest.tipo());

        return artistRepository.save(artista);
    }

    @Transactional
    public ArtistResponse update(Long id, ArtistUpdateRequest artistaUpdateRequest) {

        Artist artista = artistRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Artista não encontrado")
                );

        artista.setNome(artistaUpdateRequest.nome());
        artista.setTipo(artistaUpdateRequest.tipo());

        Artist updatedArtista = artistRepository.save(artista);

        return new ArtistResponse(
                updatedArtista.getId(),
                updatedArtista.getNome(),
                updatedArtista.getTipo(),
                updatedArtista.getAlbums()
                        .stream()
                        .map(album -> new AlbumSimpleResponse(
                                album.getId(),
                                album.getNome()
                        ))
                        .toList()
        );

    }

    @Transactional(readOnly = true)
    public List<ArtistResponse> list() {
        return artistRepository.findAll()
                .stream()
                .map(artista -> new ArtistResponse(
                        artista.getId(),
                        artista.getNome(),
                        artista.getTipo(),
                        artista.getAlbums()
                                .stream()
                                .map(album -> new AlbumSimpleResponse(
                                        album.getId(),
                                        album.getNome()
                                ))
                                .toList()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public ArtistResponse findById(Long id) {
        Artist artista = artistRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Artista não encontrado")
                );

        return new ArtistResponse(
                artista.getId(),
                artista.getNome(),
                artista.getTipo(),
                artista.getAlbums()
                        .stream()
                        .map(album -> new AlbumSimpleResponse(
                                album.getId(),
                                album.getNome()
                        ))
                        .toList()
        );

    }
}
