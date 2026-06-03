package com.nexa.auth.infra.mapper;

import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.infra.persistence.entity.UsuarioEntity;

public class UsuarioPersistenceMapper {

    private final PerfilPersistenceMapper perfilMapper;

    public UsuarioPersistenceMapper(PerfilPersistenceMapper perfilMapper) {
        this.perfilMapper = perfilMapper;
    }

    public UsuarioEntity toEntity(Usuario usuario) {
        return new UsuarioEntity(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getCriadoEm(),
                usuario.getAtivo(),
                perfilMapper.toEntity(usuario.getPerfil())
        );
    }

    public Usuario toDomain(UsuarioEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getSenha(),
                entity.getCriadoEm(),
                entity.getAtivo(),
                perfilMapper.toDomain(entity.getPerfil())
        );
    }
}
