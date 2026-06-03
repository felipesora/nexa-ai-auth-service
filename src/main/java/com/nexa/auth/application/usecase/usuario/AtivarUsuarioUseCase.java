package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.UsuarioRepository;

public class AtivarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public AtivarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void ativarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Usuário com id %s não encontrado", id)));

        usuario.ativar();
        usuarioRepository.save(usuario);
    }


}
