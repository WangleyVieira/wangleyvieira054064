package com.wangley.musicapi.service;

import com.wangley.musicapi.client.RegionalClient;
import com.wangley.musicapi.domain.entity.Regional;
import com.wangley.musicapi.dto.external.RegionalExternalResponse;
import com.wangley.musicapi.repository.RegionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegionalImportService {

    private final RegionalClient regionalClient;
    private final RegionalRepository regionalRepository;

    public RegionalImportService(
            RegionalClient regionalClient,
            RegionalRepository regionalRepository
    ) {
        this.regionalClient = regionalClient;
        this.regionalRepository = regionalRepository;
    }

    @Transactional
    public int importRegionais() {

        List<RegionalExternalResponse> regionalExternalResponses =
                regionalClient.fetchAll();

        regionalExternalResponses.forEach(regionalExternalResponse -> {
            Regional regional = new Regional();
            regional.setCodigoExterno(regionalExternalResponse.id());
            regional.setNome(regionalExternalResponse.nome());
            regional.setAtivo(true);

            regionalRepository.save(regional);

        });

        return regionalExternalResponses.size();
    }
}
