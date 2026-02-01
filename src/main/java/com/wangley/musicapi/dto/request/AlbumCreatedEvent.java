package com.wangley.musicapi.dto.request;

public record AlbumCreatedEvent(
        Long albumId,
        String name
) {}
