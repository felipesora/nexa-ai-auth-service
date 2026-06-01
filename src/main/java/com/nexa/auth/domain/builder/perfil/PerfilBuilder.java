package com.nexa.auth.domain.builder.perfil;

import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.entity.perfil.TipoPerfil;

public class PerfilBuilder {

    private Long id = 1L;
    private TipoPerfil nome = TipoPerfil.USER;

    public PerfilBuilder comId(Long id) {
        this.id = id;
        return this;
    }

    public PerfilBuilder comNome(TipoPerfil nome) {
        this.nome = nome;
        return this;
    }

    public Perfil build() {
        return new Perfil(id, nome);
    }
}
