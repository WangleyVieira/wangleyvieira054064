package com.wangley.musicapi.dto.response;

import com.wangley.musicapi.domain.enums.TypeArtist;

import java.util.List;

public record ArtistResponse(
    Long id,
    String nome,
    TypeArtist tipo,
    List<AlbumSimpleResponse> albums
) {}
