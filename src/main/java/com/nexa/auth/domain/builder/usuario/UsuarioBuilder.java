package com.nexa.auth.domain.builder.usuario;

import com.nexa.auth.domain.builder.perfil.PerfilBuilder;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.domain.entity.usuario.Usuario;

import java.time.LocalDateTime;

public class UsuarioBuilder {

    private Long id = 1L;
    private String nome = "Felipe";
    private String email = "felipe@email.com";
    private String senha = "123456";
    private LocalDateTime criadoEm = LocalDateTime.now();
    private Boolean ativo = true;
    private Perfil perfil = new PerfilBuilder().build();

    public UsuarioBuilder comId(Long id) {
        this.id = id;
        return this;
    }

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

    public UsuarioBuilder comPerfil(Perfil perfil) {
        this.perfil = perfil;
        return this;
    }

    public UsuarioBuilder comAtivo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public Usuario build() {
        return new Usuario(
                id,
                nome,
                email,
                senha,
                criadoEm,
                ativo,
                perfil
        );
    }
}
