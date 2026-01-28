package com.wangley.musicapi.controller;

import com.wangley.musicapi.domain.entity.Album;
import com.wangley.musicapi.domain.enums.TypeArtist;
import com.wangley.musicapi.dto.request.AlbumCreateRequest;
import com.wangley.musicapi.dto.response.AlbumResponse;
import com.wangley.musicapi.service.AlbumService;
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

@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;

    public  AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    /* Cria um novo álbum */
    @PostMapping
    public ResponseEntity<AlbumResponse> create(
            @Valid @RequestBody AlbumCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(albumService.create(request));
    }

    /* Busca um álbum pelo ID */
    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.findById(id));
    }

    /* Atualiza um álbum existente */
    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid AlbumCreateRequest albumCreateRequest
    ){
        return ResponseEntity.ok(albumService.update(id, albumCreateRequest));
    }

    /**
     * Lista álbuns com paginação opcional
     */
    @GetMapping
    public Page<AlbumResponse> findAll(
            Pageable pageable,
            @RequestParam(required = false) TypeArtist tipo,
            @RequestParam(required = false) String artista,
            @RequestParam(required = false) String album,
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
