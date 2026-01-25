package com.wangley.musicapi.auth.dto;

public record LoginRequest(
    String username,
    String password
) {}
