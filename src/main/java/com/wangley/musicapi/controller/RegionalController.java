package com.wangley.musicapi.controller;

import com.wangley.musicapi.dto.external.RegionalImportResponse;
import com.wangley.musicapi.dto.external.RegionalResponse;
import com.wangley.musicapi.dto.external.RegionalSyncResponse;
import com.wangley.musicapi.service.RegionalImportService;
import com.wangley.musicapi.service.RegionalSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/regionais")
@Tag(name = "Regionais")
public class RegionalController {

    private final RegionalImportService importService;
    private final RegionalSyncService regionalSyncService;

    public RegionalController(
            RegionalImportService importService,
            RegionalSyncService regionalSyncService
    ) {
        this.importService = importService;
        this.regionalSyncService = regionalSyncService;
    }

    @Operation(summary = "Importar regionais da API externa")
    @PostMapping("/importar")
    public ResponseEntity<RegionalImportResponse> importar() {

        int total = importService.importRegionais();

        return ResponseEntity.accepted().body(
                new  RegionalImportResponse(
                        "Importação realizada com sucesso.",
                        total
                )
        );
    }

    @Operation(summary = "Sincronização de regionais")
    @PostMapping("/sincronizar")
    public ResponseEntity<RegionalSyncResponse> sincronizar() {

        regionalSyncService.sincronizar();

        return ResponseEntity.accepted().body(
                new RegionalSyncResponse(
                        "Sincronização de regionais realizada com sucesso"
                )
        );
    }

    @Operation(summary = "Listar regionais importados")
    @GetMapping
    public ResponseEntity<List<RegionalResponse>> listar() {
        return ResponseEntity.ok(importService.listarAtivos());
    }
}
