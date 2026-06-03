package com.nexa.auth.application.usecase.perfil;

import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.repository.PerfilRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class CadastrarPerfilUseCase {

    private final PerfilRepository perfilRepository;

    public CadastrarPerfilUseCase(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @Transactional
    public Perfil cadastrarPerfil(Perfil perfil) {
        Optional<Perfil> perfilExiste = perfilRepository.findByNome(perfil.getNome());

        if (perfilExiste.isPresent()) {
            throw new BadRequestException("Este perfil já está cadastrado");
        }

        return perfilRepository.save(perfil);
    }
}
