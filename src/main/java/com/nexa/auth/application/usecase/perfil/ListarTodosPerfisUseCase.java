package com.nexa.auth.application.usecase.perfil;

import com.nexa.auth.application.dto.perfil.PerfilResponse;
import com.nexa.auth.application.mapper.PerfilControllerMapper;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.repository.PerfilRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTodosPerfisUseCase {

    private final PerfilRepository perfilRepository;
    private final PerfilControllerMapper mapper;

    public ListarTodosPerfisUseCase(PerfilRepository perfilRepository, PerfilControllerMapper mapper) {
        this.perfilRepository = perfilRepository;
        this.mapper = mapper;
    }

    public Page<PerfilResponse> execute(Pageable pageable) {
        return perfilRepository.findAll(pageable)
                .map(mapper::toResponse);
    }
}
