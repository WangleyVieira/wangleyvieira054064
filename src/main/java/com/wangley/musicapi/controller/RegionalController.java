package com.wangley.musicapi.controller;

import com.wangley.musicapi.dto.external.RegionalImportResponse;
import com.wangley.musicapi.service.RegionalImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/regionais")
@Tag(name = "Regionais")
public class RegionalController {

    private final RegionalImportService importService;

    public RegionalController(RegionalImportService importService) {
        this.importService = importService;
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
}
