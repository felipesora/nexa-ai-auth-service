package com.nexa.auth.domain.entity.perfil;

import com.nexa.auth.domain.exception.DomainException;

public class Perfil {

    private Long id;
    private TipoPerfil nome;

    public Perfil(Long id) {
        this.id = id;
    }

    public Perfil(Long id, TipoPerfil nome) {
        validarNome(nome);

        this.id = id;
        this.nome = nome;
    }

    private void validarNome(TipoPerfil nome) {
        if (nome == null) {
            throw new DomainException("Nome do perfil é obrigatório.");
        }
    }

    public Long getId() {
        return id;
    }

    public TipoPerfil getNome() {
        return nome;
    }
}
