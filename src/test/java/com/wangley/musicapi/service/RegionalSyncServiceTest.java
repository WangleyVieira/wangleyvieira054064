package com.wangley.musicapi.service;

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

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegionalSyncServiceTest {

    @Mock
    private RegionalClient regionalClient;

    @Mock
    private RegionalRepository regionalRepository;

    @InjectMocks
    private RegionalSyncService regionalSyncService;

    @Test
    @DisplayName("Deve inserir uma nova região quando ela não existir no banco de dados")
    void shouldInsertNewRegionWhenNotExists() {

        when(regionalClient.fetchAll()).thenReturn(List.of(
                new RegionalExternalResponse(1, "Norte")
        ));

        when(regionalRepository.findAllByAtivoTrue())
                .thenReturn(List.of());

        regionalSyncService.sincronizar();

        verify(regionalRepository, times(1))
                .save(argThat(regional ->
                        regional.getCodigoExterno().equals(1)
                                && regional.getNome().equals("Norte")
                                && regional.getAtivo()
                ));
    }

    @Test
    @DisplayName("Deve desativar a região quando ela não estiver presente na API externa.")
    void shouldInactivateRegionWhenNotPresentInExternalApi() {

        Regional existente = new Regional();
        existente.setId(1L);
        existente.setCodigoExterno(10);
        existente.setNome("Centro");
        existente.setAtivo(true);

        when(regionalClient.fetchAll()).thenReturn(List.of());
        when(regionalRepository.findAllByAtivoTrue())
                .thenReturn(List.of(existente));

        regionalSyncService.sincronizar();

        assertFalse(existente.getAtivo());

        verify(regionalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve desativar a região antiga e criar uma nova quando o nome for alterado.")
    void shouldInactivateAndCreateNewWhenNameChanges() {

        Regional existente = new Regional();
        existente.setId(1L);
        existente.setCodigoExterno(1);
        existente.setNome("Norte");
        existente.setAtivo(true);

        when(regionalClient.fetchAll()).thenReturn(List.of(
                new RegionalExternalResponse(1, "Norte Atualizado")
        ));

        when(regionalRepository.findAllByAtivoTrue())
                .thenReturn(List.of(existente));

        regionalSyncService.sincronizar();

        assertFalse(existente.getAtivo());

        verify(regionalRepository, times(1))
                .save(argThat(regional ->
                        regional.getCodigoExterno().equals(1)
                                && regional.getNome().equals("Norte Atualizado")
                                && regional.getAtivo()
                ));
    }

    @Test
    @DisplayName("Não deve fazer nada quando os dados externos forem idênticos.")
    void shouldDoNothingWhenExternalDataIsIdentical() {

        Regional existente = new Regional();
        existente.setId(1L);
        existente.setCodigoExterno(1);
        existente.setNome("Norte");
        existente.setAtivo(true);

        when(regionalClient.fetchAll()).thenReturn(List.of(
                new RegionalExternalResponse(1, "Norte")
        ));

        when(regionalRepository.findAllByAtivoTrue())
                .thenReturn(List.of(existente));

        regionalSyncService.sincronizar();

        assertTrue(existente.getAtivo());

        verify(regionalRepository, never()).save(any());
    }
}
