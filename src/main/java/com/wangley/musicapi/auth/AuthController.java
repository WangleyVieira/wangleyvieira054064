package com.wangley.musicapi.auth;

import com.wangley.musicapi.auth.dto.AuthResponse;
import com.wangley.musicapi.auth.dto.LoginRequest;
import com.wangley.musicapi.auth.dto.RefreshTokenRequest;
import com.wangley.musicapi.auth.dto.RefreshTokenResponse;
import com.wangley.musicapi.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Endpoints de autenticação e renovação")
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(
            summary = "Autenticar usuário e gerar tokens JWT",
            description = "Retornar access token e refresh token para autenticação"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {

        if (!"admin".equals(request.username()) || !"admin".equals(request.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = jwtTokenProvider.generateAccessToken(request.username());
        String refreshToken = jwtTokenProvider.generateRefreshToken(request.username());

        return ResponseEntity.ok(
                new AuthResponse(accessToken, refreshToken, "Bearer")
        );
    }

    @Operation(
            summary = "Renovar access token",
            description = "Gera um novo access token a partir de um refresh token"
    )
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody RefreshTokenRequest request
    ) {

        String refreshToken = request.refreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!jwtTokenProvider.isRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        String newAccessToken = jwtTokenProvider.generateAccessToken(username);

        return ResponseEntity.ok(
                new AuthResponse(newAccessToken, null, "Bearer")
        );
    }
}