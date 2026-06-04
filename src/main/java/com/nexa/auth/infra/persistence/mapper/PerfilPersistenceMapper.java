package com.nexa.auth.infra.persistence.mapper;

import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.infra.persistence.entity.PerfilEntity;

public class PerfilPersistenceMapper {

    public PerfilEntity toEntity(Perfil perfil) {
        return new PerfilEntity(
                perfil.getId(),
                perfil.getNome()
        );
    }

    public Perfil toDomain(PerfilEntity entity) {
        return new Perfil(
                entity.getId(),
                entity.getNome()
        );
    }
}
