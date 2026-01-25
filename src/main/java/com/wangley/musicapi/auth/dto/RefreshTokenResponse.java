package com.wangley.musicapi.auth.dto;

public record RefreshTokenResponse(
    String accessToken,
    String tokenType
) {
}
