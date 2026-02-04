package com.wangley.musicapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import com.wangley.musicapi.client.RegionalClient;
import com.wangley.musicapi.domain.entity.Regional;
import com.wangley.musicapi.dto.external.RegionalExternalResponse;
import com.wangley.musicapi.repository.RegionalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegionalImportServiceTest {

    @Mock
    private RegionalClient regionalClient;

    @Mock
    private RegionalRepository regionalRepository;

    @InjectMocks
    private RegionalImportService regionalImportService;

    @Test
    @DisplayName("Deve importar todas as regionais retornadas pelo client")
    void shouldImportAllRegionais() {

        var resp1 = new RegionalExternalResponse(1, "Várzea Grande");
        var resp2 = new RegionalExternalResponse(2, "Livramento");

        when(regionalClient.fetchAll()).thenReturn(List.of(resp1, resp2));

        int totalImportado = regionalImportService.importRegionais();

        assertEquals(2, totalImportado);

        verify(regionalRepository, times(2)).save(any(Regional.class));
    }

    @Test
    @DisplayName("Deve retornar zero quando a API externa estiver vazia")
    void shouldReturnZeroWhenApiIsEmpty() {

        when(regionalClient.fetchAll()).thenReturn(List.of());

        int totalImportado = regionalImportService.importRegionais();

        assertEquals(0, totalImportado);
        verify(regionalRepository, never()).save(any());
    }
}