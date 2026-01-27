package com.wangley.musicapi.dto.response;

import com.wangley.musicapi.domain.enums.TypeArtist;

public record ArtistResumeResponse(
        Long id,
        String nome,
        TypeArtist tipo
) {}
