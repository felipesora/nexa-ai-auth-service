package com.nexa.auth.application.mapper;

import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.application.dto.usuario.UsuarioRequest;
import com.nexa.auth.application.dto.perfil.PerfilResponse;
import com.nexa.auth.application.dto.usuario.UsuarioResponse;

import java.time.LocalDateTime;

public class UsuarioControllerMapper {

    public Usuario toDomain(UsuarioRequest request) {
        return new Usuario(
                null,
                request.nome(),
                request.email(),
                request.senha(),
                LocalDateTime.now(),
                true,
                null
        );
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        PerfilResponse perfil = new PerfilResponse(usuario.getPerfil().getId(), usuario.getPerfil().getNome());

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCriadoEm(),
                usuario.getAtivo(),
                perfil
        );
    }
}
