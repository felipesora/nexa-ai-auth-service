package com.nexa.auth.presentation.request.perfil;

import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import jakarta.validation.constraints.NotNull;

public record PerfilRequest(
        @NotNull(message = "Nome do perfil é obrigatório")
        TipoPerfil nome
) {
}
