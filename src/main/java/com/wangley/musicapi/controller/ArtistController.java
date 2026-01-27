package com.wangley.musicapi.controller;

import com.wangley.musicapi.domain.entity.Artist;
import com.wangley.musicapi.dto.request.ArtistCreateRequest;
import com.wangley.musicapi.dto.request.ArtistUpdateRequest;
import com.wangley.musicapi.dto.response.ArtistResponse;
import com.wangley.musicapi.service.ArtistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistaService;

    public ArtistController(ArtistService artistaService) {
        this.artistaService = artistaService;
    }

    /* Cria um novo artista */
    @PostMapping
    public ResponseEntity<Artist> create(
            @Valid @RequestBody ArtistCreateRequest artistaCreateRequest
    ){
        Artist artista = artistaService.create(artistaCreateRequest);
        return  ResponseEntity.status(HttpStatus.CREATED).body(artista);
    }

    /* Atualiza um artista existente */
    @PutMapping("/{id}")
    public ResponseEntity<ArtistResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ArtistUpdateRequest artistaUpdateRequest
    ){
        ArtistResponse artistResponse = artistaService.update(id, artistaUpdateRequest);
        return ResponseEntity.ok(artistResponse);
    }

    /* Lista todos os artistas */
    @GetMapping
    public ResponseEntity<List<ArtistResponse>> findAll(){
        return ResponseEntity.ok(artistaService.list());
    }

    /* Busca um artista por ID */
    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponse> findById(@PathVariable Long id) {
        ArtistResponse artistResponse = artistaService.findById(id);
        return ResponseEntity.ok(artistResponse);
    }
}
