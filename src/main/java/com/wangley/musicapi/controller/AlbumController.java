package com.wangley.musicapi.controller;

import com.wangley.musicapi.domain.entity.Album;
import com.wangley.musicapi.domain.enums.TypeArtist;
import com.wangley.musicapi.dto.request.AlbumCreateRequest;
import com.wangley.musicapi.dto.response.AlbumResponse;
import com.wangley.musicapi.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Álbuns", description = "Endpoints para gerenciamento de álbuns")
@RestController
@RequestMapping("/v1/albums")
public class AlbumController {

    private final AlbumService albumService;

    public  AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @Operation(summary = "Cadastrar um novo álbum")
    @PostMapping
    public ResponseEntity<AlbumResponse> create(
            @Valid @RequestBody AlbumCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(albumService.create(request));
    }

    @Operation(summary = "Buscar álbum por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponse> findById(
            @Parameter(description = "ID do álbum")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(albumService.findById(id));
    }

    @Operation(summary = "Atualizar dados de um álbum")
    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponse> update(
            @Parameter(description = "ID do álbum")
            @PathVariable Long id,
            @RequestBody @Valid AlbumCreateRequest albumCreateRequest
    ){
        return ResponseEntity.ok(albumService.update(id, albumCreateRequest));
    }

    @Operation(summary = "Listar álbuns com paginação, filtros e ordenação")
    @GetMapping
    public Page<AlbumResponse> findAll(
            @Parameter(description = "Parâmetros de paginação")
            Pageable pageable,

            @Parameter(description = "Tipo de artista (CANTOR ou BANDA)")
            @RequestParam(required = false) TypeArtist tipo,

            @Parameter(description = "Nome do artista")
            @RequestParam(required = false) String artista,

            @Parameter(description = "Nome do álbum")
            @RequestParam(required = false) String album,

            @Parameter(description = "Direção da ordenação (ASC ou DES)")
            @RequestParam(defaultValue = "ASC")Sort.Direction sortDirection
            ) {
        return albumService.findAll(
                pageable,
                tipo,
                artista,
                album,
                sortDirection
        );
    }
}
