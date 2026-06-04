package com.nexa.auth.application.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank
        String email,

        @NotBlank
        String senha
) {
}
