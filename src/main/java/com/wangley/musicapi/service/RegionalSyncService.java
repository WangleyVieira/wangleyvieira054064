package com.wangley.musicapi.service;

import com.wangley.musicapi.client.RegionalClient;
import com.wangley.musicapi.domain.entity.Regional;
import com.wangley.musicapi.dto.external.RegionalExternalResponse;
import com.wangley.musicapi.repository.RegionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RegionalSyncService {

    private final RegionalClient regionalClient;
    private final RegionalRepository regionalRepository;

    public RegionalSyncService(
            RegionalClient regionalClient,
            RegionalRepository regionalRepository
    ) {
        this.regionalClient = regionalClient;
        this.regionalRepository = regionalRepository;
    }

    @Transactional
    public void sincronizar() {

        List<RegionalExternalResponse> externals =
                regionalClient.fetchAll();

        Map<Integer, RegionalExternalResponse> externalMap =
                externals.stream()
                        .collect(Collectors.toMap(
                                RegionalExternalResponse::id,
                                Function.identity()
                        ));

        List<Regional> atuais = regionalRepository.findAllByAtivoTrue();

        for (Regional regional : atuais) {

            RegionalExternalResponse external =
                    externalMap.remove(regional.getCodigoExterno());

            if (external == null) {
                regional.setAtivo(false);
                continue;
            }

            if (!regional.getNome().equals(external.nome())) {
                regional.setAtivo(false);

                Regional novo = new Regional();
                novo.setCodigoExterno(external.id());
                novo.setNome(external.nome());
                novo.setAtivo(true);

                regionalRepository.save(novo);

            }
        }

        externalMap.values().forEach(external -> {
            Regional novo = new Regional();
            novo.setCodigoExterno(external.id());
            novo.setNome(external.nome());
            novo.setAtivo(true);

            regionalRepository.save(novo);
        });
    }
}
