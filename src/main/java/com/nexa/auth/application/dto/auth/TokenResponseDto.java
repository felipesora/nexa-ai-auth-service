package com.nexa.auth.application.dto.auth;

public record TokenResponseDto(String token, long expiresIn) {
}
