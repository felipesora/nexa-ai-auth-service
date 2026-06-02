package com.nexa.auth.presentation.response.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nexa.auth.presentation.response.perfil.PerfilResponse;

import java.time.LocalDateTime;

@JsonPropertyOrder({ "id_usuario", "nome", "email", "criado_em", "ativo", "perfil" })
public record UsuarioResponse(
        @JsonProperty("id_usuario")
        Long id,

        String nome,

        String email,

        @JsonProperty("criado_em")
        LocalDateTime criadoEm,

        Boolean ativo,

        PerfilResponse perfil
) {
}
