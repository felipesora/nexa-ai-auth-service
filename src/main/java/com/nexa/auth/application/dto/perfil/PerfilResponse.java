package com.nexa.auth.application.dto.perfil;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nexa.auth.domain.entity.perfil.TipoPerfil;

public record PerfilResponse(
        @JsonProperty("id_perfil")
        Long id,

        TipoPerfil nome
) {
}
