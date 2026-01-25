package com.wangley.musicapi.auth.dto;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    String tokenType
) {}
