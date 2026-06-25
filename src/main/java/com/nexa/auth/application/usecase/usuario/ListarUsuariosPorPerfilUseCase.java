package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.dto.usuario.UsuarioResponse;
import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.mapper.UsuarioControllerMapper;
import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.PerfilRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarUsuariosPorPerfilUseCase {

    private final PerfilRepository perfilRepository;
    private final UsuarioControllerMapper mapper;

    public ListarUsuariosPorPerfilUseCase(PerfilRepository perfilRepository, UsuarioControllerMapper mapper) {
        this.perfilRepository = perfilRepository;
        this.mapper = mapper;
    }

    public Page<UsuarioResponse> execute(String nomePerfil, Pageable pageable) {

        TipoPerfil tipoPerfil = converterParaTipoPerfil(nomePerfil);

        return perfilRepository.findUsuariosByPerfil(tipoPerfil, pageable)
                .map(mapper::toResponse);
    }

    private TipoPerfil converterParaTipoPerfil(String nomePerfil) {
        try {
            return TipoPerfil.valueOf(nomePerfil.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(String.format("Perfil '%s' é inválido", nomePerfil));
        }
    }
}
