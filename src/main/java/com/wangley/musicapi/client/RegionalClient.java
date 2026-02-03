package com.wangley.musicapi.client;

import com.wangley.musicapi.dto.external.RegionalExternalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RegionalClient {

    private final RestTemplate restTemplate;

    @Value("${regionais.api.url}")
    private String url;

    public RegionalClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RegionalExternalResponse> fetchAll() {

        ResponseEntity<RegionalExternalResponse[]> response =
                restTemplate.getForEntity(url, RegionalExternalResponse[].class);

        return Optional.ofNullable(response.getBody())
                .map(Arrays::asList)
                .orElse(List.of());
    }

}
