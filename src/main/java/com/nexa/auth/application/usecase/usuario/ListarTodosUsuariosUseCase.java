package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTodosUsuariosUseCase {

    private final UsuarioRepository usuarioRepository;

    public ListarTodosUsuariosUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Page<Usuario> listarTodosUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }
}
