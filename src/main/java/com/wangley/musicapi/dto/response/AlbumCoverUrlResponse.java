package com.wangley.musicapi.dto.response;

import java.time.LocalDateTime;

public record AlbumCoverUrlResponse(
        String url,
        LocalDateTime expiresAt
) {}
