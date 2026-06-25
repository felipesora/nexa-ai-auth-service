package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.dto.usuario.UsuarioResponse;
import com.nexa.auth.application.mapper.UsuarioControllerMapper;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTodosUsuariosUseCase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioControllerMapper mapper;

    public ListarTodosUsuariosUseCase(UsuarioRepository usuarioRepository, UsuarioControllerMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    public Page<UsuarioResponse> execute(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(mapper::toResponse);
    }
}
