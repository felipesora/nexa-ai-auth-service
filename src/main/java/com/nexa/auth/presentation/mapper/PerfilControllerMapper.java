package com.nexa.auth.presentation.mapper;

import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.presentation.request.perfil.PerfilRequest;
import com.nexa.auth.presentation.response.perfil.PerfilResponse;

public class PerfilControllerMapper {

    public Perfil toDomain(PerfilRequest request) {
        return new Perfil(null, request.nome());
    }

    public PerfilResponse toResponse(Perfil perfil) {
        return new PerfilResponse(perfil.getId(), perfil.getNome());
    }
}
