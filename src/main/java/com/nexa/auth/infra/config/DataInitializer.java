package com.nexa.auth.infra.config;

import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.PerfilRepository;
import com.nexa.auth.domain.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (perfilRepository.findByNome(TipoPerfil.USER).isEmpty()) {
            perfilRepository.save(
                    new Perfil(null, TipoPerfil.USER)
            );
        }

        if (perfilRepository.findByNome(TipoPerfil.ADMIN).isEmpty()) {
            perfilRepository.save(
                    new Perfil(null, TipoPerfil.ADMIN)
            );
        }

        Perfil perfilAdmin = perfilRepository
                .findByNome(TipoPerfil.ADMIN)
                .orElseThrow();

        if (usuarioRepository.findByEmail("admin@email.com").isEmpty()) {

            usuarioRepository.save(
                    new Usuario(
                            null,
                            "ADMIN",
                            "admin@email.com",
                            passwordEncoder.encode("admin123"),
                            LocalDateTime.now(),
                            true,
                            perfilAdmin
                    )
            );
        }
    }
}
