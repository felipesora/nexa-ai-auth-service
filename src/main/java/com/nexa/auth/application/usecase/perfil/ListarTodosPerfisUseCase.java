package com.nexa.auth.application.usecase.perfil;

import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.repository.PerfilRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTodosPerfisUseCase {

    private final PerfilRepository perfilRepository;

    public ListarTodosPerfisUseCase(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public Page<Perfil> listarTodosPerfis(Pageable pageable) {
        return perfilRepository.findAll(pageable);
    }
}
