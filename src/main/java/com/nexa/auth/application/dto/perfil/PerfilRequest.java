package com.nexa.auth.application.dto.perfil;

import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import jakarta.validation.constraints.NotNull;

public record PerfilRequest(
        @NotNull(message = "Nome do perfil é obrigatório")
        TipoPerfil nome
) {
}
