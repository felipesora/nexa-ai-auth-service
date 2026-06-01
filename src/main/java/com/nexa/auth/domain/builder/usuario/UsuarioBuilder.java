package com.nexa.auth.domain.builder.usuario;

import com.nexa.auth.domain.entity.usuario.Usuario;

import java.time.LocalDateTime;

public class UsuarioBuilder {

    private Long id = 1L;
    private String nome = "Felipe";
    private String email = "felipe@email.com";
    private String senha = "123456";
    private LocalDateTime criadoEm = LocalDateTime.now();
    private Boolean ativo = true;

    public UsuarioBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public UsuarioBuilder comEmail(String email) {
        this.email = email;
        return this;
    }

    public UsuarioBuilder comSenha(String senha) {
        this.senha = senha;
        return this;
    }

    public Usuario build() {
        return new Usuario(
                id,
                nome,
                email,
                senha,
                criadoEm,
                ativo
        );
    }
}
