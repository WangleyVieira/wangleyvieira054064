package com.wangley.musicapi.controller;

import com.wangley.musicapi.domain.entity.Artist;
import com.wangley.musicapi.dto.request.ArtistCreateRequest;
import com.wangley.musicapi.dto.request.ArtistUpdateRequest;
import com.wangley.musicapi.dto.response.ArtistResponse;
import com.wangley.musicapi.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Artistas", description = "Endpoints para gerenciamento de artistas")
@RestController
@RequestMapping("/v1/artists")
public class ArtistController {

    private final ArtistService artistaService;

    public ArtistController(ArtistService artistaService) {
        this.artistaService = artistaService;
    }

    @Operation(summary = "Cadastrar um novo artista")
    @PostMapping
    public ResponseEntity<Artist> create(
            @Valid @RequestBody ArtistCreateRequest artistaCreateRequest
    ){
        Artist artista = artistaService.create(artistaCreateRequest);
        return  ResponseEntity.status(HttpStatus.CREATED).body(artista);
    }

    @Operation(summary = "Atualizar dados de um artista")
    @PutMapping("/{id}")
    public ResponseEntity<ArtistResponse> update(
            @Parameter(description = "ID do artista")
            @PathVariable Long id,
            @Valid @RequestBody ArtistUpdateRequest artistaUpdateRequest
    ){
        ArtistResponse artistResponse = artistaService.update(id, artistaUpdateRequest);
        return ResponseEntity.ok(artistResponse);
    }

    @Operation(summary = "Listar todos os artistas")
    @GetMapping
    public ResponseEntity<List<ArtistResponse>> findAll(){
        return ResponseEntity.ok(artistaService.list());
    }


    @Operation(summary = "Buscar artista por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponse> findById(
            @Parameter(description = "ID do artista")
            @PathVariable Long id
    ) {
        ArtistResponse artistResponse = artistaService.findById(id);
        return ResponseEntity.ok(artistResponse);
    }
}
