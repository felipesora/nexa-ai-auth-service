package com.nexa.auth.presentation.mapper;

import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.presentation.request.usuario.UsuarioRequest;
import com.nexa.auth.presentation.response.perfil.PerfilResponse;
import com.nexa.auth.presentation.response.usuario.UsuarioResponse;

import java.time.LocalDateTime;

public class UsuarioControllerMapper {

    public Usuario toDomain(UsuarioRequest request) {
        Perfil perfil = new Perfil(request.idPerfil());

        return new Usuario(
                null,
                request.nome(),
                request.email(),
                request.senha(),
                LocalDateTime.now(),
                true,
                perfil
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
