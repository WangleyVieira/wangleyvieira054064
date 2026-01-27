package com.wangley.musicapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.Set;

public record AlbumCreateRequest(

    @NotBlank
    String nome,

    LocalDate dataLancamento,

    @NotEmpty
    Set<Long> artistIds
) {}
