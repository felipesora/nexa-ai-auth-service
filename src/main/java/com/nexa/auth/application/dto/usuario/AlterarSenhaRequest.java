package com.nexa.auth.application.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlterarSenhaRequest(
        @JsonProperty("senha_atual")
        @NotBlank(message = "A senha atual é obrigatória")
        String senhaAtual,

        @JsonProperty("nova_senha")
        @NotBlank(message = "A nova senha é obrigatória")
        @Size(min = 6, max = 100, message = "A nova senha deve ter entre 6 e 100 caracteres")
        String novaSenha
) {
}
