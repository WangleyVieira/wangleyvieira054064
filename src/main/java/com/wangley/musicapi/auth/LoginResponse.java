package com.wangley.musicapi.auth;

public record LoginResponse(
    String token,
    String type
){}
