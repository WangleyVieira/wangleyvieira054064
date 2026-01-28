package com.wangley.musicapi.service;

import com.wangley.musicapi.domain.entity.Album;
import com.wangley.musicapi.domain.entity.Artist;
import com.wangley.musicapi.domain.enums.TypeArtist;
import com.wangley.musicapi.dto.request.AlbumCreateRequest;
import com.wangley.musicapi.dto.response.AlbumResponse;
import com.wangley.musicapi.dto.response.ArtistResumeResponse;
import com.wangley.musicapi.exception.ResourceNotFoundException;
import com.wangley.musicapi.repository.AlbumRepository;
import com.wangley.musicapi.repository.ArtistRepository;
import com.wangley.musicapi.repository.specification.AlbumSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public AlbumService(AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    @Transactional
    public AlbumResponse create(AlbumCreateRequest albumCreateRequest) {

        Set<Artist> artistas = new HashSet<>(
                artistRepository.findAllById(albumCreateRequest.artistIds())
        );

        if (artistas.size() != albumCreateRequest.artistIds().size()) {
            throw new IllegalArgumentException("Um ou mais artistas não encontrados");
        }

        Album album = new Album();
        album.setNome(albumCreateRequest.nome());
        album.setDataLancamento(albumCreateRequest.dataLancamento());
        album.setArtistas(artistas);

        Album savedAlbum = albumRepository.save(album);

        return albumCreateResponse(savedAlbum);

    }

    private AlbumResponse albumCreateResponse(Album album) {
        return new AlbumResponse(
                album.getId(),
                album.getNome(),
                album.getDataLancamento(),
                album.getArtistas()
                        .stream()
                        .map(a -> new ArtistResumeResponse(
                                a.getId(),
                                a.getNome(),
                                a.getTipo()
                        ))
                        .collect(Collectors.toSet())
        );
    }

    @Transactional(readOnly = true)
    public AlbumResponse findById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Álbum não encontrado")
                );

        return albumCreateResponse(album);
    }

    @Transactional
    public AlbumResponse update(Long id, AlbumCreateRequest albumCreateRequest) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Álbum não encontrado")
                );

        Set<Artist> artistas = new HashSet<>(
                artistRepository.findAllById(albumCreateRequest.artistIds())
        );

        if (artistas.size() != albumCreateRequest.artistIds().size()) {
            throw new IllegalArgumentException("Um ou mais artistas não encontrados");
        }

        album.setNome(albumCreateRequest.nome());
        album.setDataLancamento(albumCreateRequest.dataLancamento());
        album.setArtistas(artistas);

        Album updatedAlbum = albumRepository.save(album);

        return albumCreateResponse(updatedAlbum);
    }

    @Transactional(readOnly = true)
    public Page<AlbumResponse> findAll(
            Pageable pageable,
            TypeArtist tipo,
            String nomeArtista,
            String nomeAlbum,
            Sort.Direction sortDirection
    ) {
        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Specification<Album> spec = AlbumSpecification.filter(
                tipo, nomeArtista, nomeAlbum, sortDirection
        );

        Page<Album> page = albumRepository.findAll(spec, pageRequest);

        return page.map(this::albumCreateResponse);
    }
}
