package com.nexa.auth.infra.config;

import com.nexa.auth.application.usecase.perfil.CadastrarPerfilUseCase;
import com.nexa.auth.application.usecase.perfil.ListarTodosPerfisUseCase;
import com.nexa.auth.domain.repository.PerfilRepository;
import com.nexa.auth.application.mapper.PerfilControllerMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PerfilBeanConfig {

    @Bean
    CadastrarPerfilUseCase cadastrarPerfilUseCase(PerfilRepository perfilRepository, PerfilControllerMapper mapper) {
        return new CadastrarPerfilUseCase(perfilRepository, mapper);
    }

    @Bean
    ListarTodosPerfisUseCase listarTodosPerfisUseCase(PerfilRepository perfilRepository, PerfilControllerMapper mapper) {
        return new ListarTodosPerfisUseCase(perfilRepository, mapper);
    }

    @Bean
    PerfilControllerMapper perfilControllerMapper() {
        return new PerfilControllerMapper();
    }
}
