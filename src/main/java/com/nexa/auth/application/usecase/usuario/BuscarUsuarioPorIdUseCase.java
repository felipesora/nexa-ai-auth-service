package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.dto.usuario.UsuarioResponse;
import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.application.mapper.UsuarioControllerMapper;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.UsuarioRepository;

public class BuscarUsuarioPorIdUseCase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioControllerMapper mapper;

    public BuscarUsuarioPorIdUseCase(UsuarioRepository usuarioRepository, UsuarioControllerMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    public UsuarioResponse execute(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Usuário com id %s não encontrado", id)));

        return mapper.toResponse(usuario);
    }
}
