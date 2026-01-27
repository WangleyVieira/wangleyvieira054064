package com.wangley.musicapi.controller;

import com.wangley.musicapi.domain.entity.Album;
import com.wangley.musicapi.dto.request.AlbumCreateRequest;
import com.wangley.musicapi.dto.response.AlbumResponse;
import com.wangley.musicapi.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    /* Lista todos os álbuns e os cantores/bandas relacionados */
    @GetMapping
    public ResponseEntity<List<AlbumResponse>> findAll() {
        return ResponseEntity.ok(albumService.findAll());
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
}
