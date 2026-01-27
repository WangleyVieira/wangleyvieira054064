package com.wangley.musicapi.dto.request;

import com.wangley.musicapi.domain.enums.TypeArtist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArtistUpdateRequest(
    @NotBlank(message = "Nome é obrigatório")
    String nome,

    @NotNull(message = "Tipo é obrigatório")
    TypeArtist tipo
) {}

