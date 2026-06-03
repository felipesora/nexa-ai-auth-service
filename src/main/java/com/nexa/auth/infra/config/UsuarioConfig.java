package com.nexa.auth.infra.config;

import com.nexa.auth.application.usecase.usuario.*;
import com.nexa.auth.domain.repository.PerfilRepository;
import com.nexa.auth.domain.repository.UsuarioRepository;
import com.nexa.auth.infra.mapper.PerfilPersistenceMapper;
import com.nexa.auth.infra.mapper.UsuarioPersistenceMapper;
import com.nexa.auth.infra.persistence.adapter.JpaPerfilRepository;
import com.nexa.auth.infra.persistence.adapter.JpaUsuarioRepository;
import com.nexa.auth.infra.persistence.repository.SpringDataPerfilRepository;
import com.nexa.auth.infra.persistence.repository.SpringDataUsuarioRepository;
import com.nexa.auth.presentation.mapper.UsuarioControllerMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsuarioConfig {

    @Bean
    CadastrarUsuarioUseCase cadastrarUsuarioUseCase(UsuarioRepository usuarioRepository,
                                                    PerfilRepository perfilRepository) {
        return new CadastrarUsuarioUseCase(usuarioRepository, perfilRepository);
    }

    @Bean
    ListarTodosUsuariosUseCase listarTodosUsuariosUseCase(UsuarioRepository usuarioRepository) {
        return new ListarTodosUsuariosUseCase(usuarioRepository);
    }

    @Bean
    BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase(UsuarioRepository usuarioRepository) {
        return new BuscarUsuarioPorIdUseCase(usuarioRepository);
    }

    @Bean
    AtualizarUsuarioUseCase atualizarUsuarioUseCase(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository) {
        return new AtualizarUsuarioUseCase(usuarioRepository, perfilRepository);
    }

    @Bean
    DesativarUsuarioUseCase desativarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        return new DesativarUsuarioUseCase(usuarioRepository);
    }

    @Bean
    AtivarUsuarioUseCase ativarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        return new AtivarUsuarioUseCase(usuarioRepository);
    }

    @Bean
    JpaUsuarioRepository jpaUsuarioRepository(SpringDataUsuarioRepository usuarioRepository,
                                              UsuarioPersistenceMapper mapper) {
        return new JpaUsuarioRepository(usuarioRepository, mapper);
    }

    @Bean
    JpaPerfilRepository jpaPerfilRepository(SpringDataPerfilRepository perfilRepository,
                                            SpringDataUsuarioRepository usuarioRepository,
                                            PerfilPersistenceMapper perfilMapper,
                                            UsuarioPersistenceMapper usuarioMapper) {
        return new JpaPerfilRepository(perfilRepository, usuarioRepository, perfilMapper, usuarioMapper);
    }

    @Bean
    UsuarioPersistenceMapper usuarioPersistenceMapper(PerfilPersistenceMapper perfilMapper) {
        return new UsuarioPersistenceMapper(perfilMapper);
    }

    @Bean
    PerfilPersistenceMapper perfilPersistenceMapper() {
        return new PerfilPersistenceMapper();
    }

    @Bean
    UsuarioControllerMapper usuarioMapper() {
        return new UsuarioControllerMapper();
    }
}
