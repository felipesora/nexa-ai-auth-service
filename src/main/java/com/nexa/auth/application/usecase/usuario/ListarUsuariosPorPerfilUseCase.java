package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.PerfilRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarUsuariosPorPerfilUseCase {

    private final PerfilRepository perfilRepository;

    public ListarUsuariosPorPerfilUseCase(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public Page<Usuario> listarUsuariosPorPerfil(String nomePerfil, Pageable pageable) {

        TipoPerfil tipoPerfil = converterParaTipoPerfil(nomePerfil);

        return perfilRepository.findUsuariosByPerfil(tipoPerfil, pageable);
    }

    private TipoPerfil converterParaTipoPerfil(String nomePerfil) {
        try {
            return TipoPerfil.valueOf(nomePerfil.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(String.format("Perfil '%s' é inválido", nomePerfil));
        }
    }
}
