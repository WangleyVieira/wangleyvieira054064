package com.wangley.musicapi.dto.request;

import com.wangley.musicapi.domain.enums.TypeArtist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArtistCreateRequest(
    @NotBlank(message = "O nome do artista é obrigatório")
    String nome,

    @NotNull(message = "O tipo do artista é obrigatório")
    TypeArtist tipo
) {}
