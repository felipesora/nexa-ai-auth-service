package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.UsuarioRepository;

public class BuscarUsuarioPorIdUseCase {

    private final UsuarioRepository usuarioRepository;

    public BuscarUsuarioPorIdUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Usuário com id %s não encontrado", id)));
    }
}
