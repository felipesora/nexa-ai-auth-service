package com.nexa.auth.infra.config;

import com.nexa.auth.application.usecase.usuario.*;
import com.nexa.auth.domain.repository.PerfilRepository;
import com.nexa.auth.domain.repository.UsuarioRepository;
import com.nexa.auth.infra.persistence.mapper.PerfilPersistenceMapper;
import com.nexa.auth.infra.persistence.mapper.UsuarioPersistenceMapper;
import com.nexa.auth.infra.persistence.adapter.JpaPerfilRepository;
import com.nexa.auth.infra.persistence.adapter.JpaUsuarioRepository;
import com.nexa.auth.infra.persistence.repository.SpringDataPerfilRepository;
import com.nexa.auth.infra.persistence.repository.SpringDataUsuarioRepository;
import com.nexa.auth.application.mapper.UsuarioControllerMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuarioBeanConfig {

    @Bean
    ListarTodosUsuariosUseCase listarTodosUsuariosUseCase(UsuarioRepository usuarioRepository,
                                                          UsuarioControllerMapper mapper) {
        return new ListarTodosUsuariosUseCase(usuarioRepository, mapper);
    }

    @Bean
    ListarUsuariosPorPerfilUseCase listarUsuariosPorPerfilUseCase(PerfilRepository perfilRepository,
                                                                  UsuarioControllerMapper mapper) {
        return new ListarUsuariosPorPerfilUseCase(perfilRepository, mapper);
    }

    @Bean
    BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase(UsuarioRepository usuarioRepository,
                                                        UsuarioControllerMapper mapper) {
        return new BuscarUsuarioPorIdUseCase(usuarioRepository, mapper);
    }

    @Bean
    AtualizarUsuarioUseCase atualizarUsuarioUseCase(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, PasswordEncoder passwordEncoder) {
        return new AtualizarUsuarioUseCase(usuarioRepository, perfilRepository, passwordEncoder);
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
