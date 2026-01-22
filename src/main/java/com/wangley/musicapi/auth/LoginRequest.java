package com.wangley.musicapi.auth;

public record LoginRequest(
    String username,
    String password
) {}
