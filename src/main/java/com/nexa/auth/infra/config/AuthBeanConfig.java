package com.nexa.auth.infra.config;

import com.nexa.auth.application.usecase.auth.CadastrarUsuarioUseCase;
import com.nexa.auth.application.usecase.auth.RealizarLoginUseCase;
import com.nexa.auth.domain.repository.PerfilRepository;
import com.nexa.auth.domain.repository.UsuarioRepository;
import com.nexa.auth.infra.security.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthBeanConfig {

    @Bean
    CadastrarUsuarioUseCase cadastrarUsuarioUseCase(UsuarioRepository usuarioRepository,
                                                    PerfilRepository perfilRepository,
                                                    PasswordEncoder passwordEncoder) {
        return new CadastrarUsuarioUseCase(usuarioRepository, perfilRepository, passwordEncoder);
    }

    @Bean
    RealizarLoginUseCase fazerLoginUseCase(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        return new RealizarLoginUseCase(authenticationManager, tokenProvider);
    }
}
