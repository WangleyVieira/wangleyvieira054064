package com.wangley.musicapi.dto.response;

import java.time.LocalDate;
import java.util.Set;

public record AlbumResponse(
        Long id,
        String nome,
        LocalDate dataLancamento,
        Set<ArtistResumeResponse> artistas
) {}
