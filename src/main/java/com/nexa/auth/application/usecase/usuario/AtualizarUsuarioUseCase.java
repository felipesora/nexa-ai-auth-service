package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.dto.usuario.UsuarioRequest;
import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.PerfilRepository;
import com.nexa.auth.domain.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class AtualizarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public AtualizarUsuarioUseCase(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void execute(Long id, UsuarioRequest request) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Usuário com id %s não encontrado", id)));

        Optional<Usuario> emailExistente = usuarioRepository.findByEmail(request.email());
        if (emailExistente.isPresent() && !emailExistente.get().getId().equals(id)) {
            throw new BadRequestException("Este email já está cadastrado");
        }

        usuarioExistente.setNome(request.nome());
        usuarioExistente.setEmail(request.email());
        usuarioExistente.setSenha(passwordEncoder.encode(request.senha()));

        if (request.idPerfil() != null) {
            var perfil = perfilRepository.findById(request.idPerfil())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Perfil com id %s não encontrado", request.idPerfil())));
            usuarioExistente.setPerfil(perfil);
        }

        usuarioRepository.save(usuarioExistente);
    }
}
