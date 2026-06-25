package com.nexa.auth.application.usecase.perfil;

import com.nexa.auth.application.dto.perfil.PerfilRequest;
import com.nexa.auth.application.dto.perfil.PerfilResponse;
import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.mapper.PerfilControllerMapper;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.repository.PerfilRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class CadastrarPerfilUseCase {

    private final PerfilRepository perfilRepository;
    private final PerfilControllerMapper mapper;

    public CadastrarPerfilUseCase(PerfilRepository perfilRepository, PerfilControllerMapper mapper) {
        this.perfilRepository = perfilRepository;
        this.mapper = mapper;
    }

    @Transactional
    public PerfilResponse execute(PerfilRequest request) {
        Optional<Perfil> perfilExiste = perfilRepository.findByNome(request.nome());

        if (perfilExiste.isPresent()) {
            throw new BadRequestException("Este perfil já está cadastrado");
        }

        Perfil salvo = perfilRepository.save(mapper.toDomain(request));
        return mapper.toResponse(salvo);
    }
}
