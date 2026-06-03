package com.nexa.auth.infra.config;

import com.nexa.auth.application.usecase.perfil.CadastrarPerfilUseCase;
import com.nexa.auth.application.usecase.perfil.ListarTodosPerfisUseCase;
import com.nexa.auth.domain.repository.PerfilRepository;
import com.nexa.auth.presentation.mapper.PerfilControllerMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PerfilConfig {

    @Bean
    CadastrarPerfilUseCase cadastrarPerfilUseCase(PerfilRepository perfilRepository) {
        return new CadastrarPerfilUseCase(perfilRepository);
    }

    @Bean
    ListarTodosPerfisUseCase listarTodosPerfisUseCase(PerfilRepository perfilRepository) {
        return new ListarTodosPerfisUseCase(perfilRepository);
    }

    @Bean
    PerfilControllerMapper perfilControllerMapper() {
        return new PerfilControllerMapper();
    }
}
