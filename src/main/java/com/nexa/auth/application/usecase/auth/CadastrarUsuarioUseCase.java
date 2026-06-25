package com.nexa.auth.application.usecase.auth;

import com.nexa.auth.application.dto.usuario.UsuarioRequest;
import com.nexa.auth.application.dto.usuario.UsuarioResponse;
import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.application.mapper.UsuarioControllerMapper;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.PerfilRepository;
import com.nexa.auth.domain.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class CadastrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioControllerMapper mapper;

    public CadastrarUsuarioUseCase(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, PasswordEncoder passwordEncoder, UsuarioControllerMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Transactional
    public UsuarioResponse execute(UsuarioRequest request) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(request.email());

        if (usuarioExistente.isPresent()) {
            throw new BadRequestException("Este email já está cadastrado");
        }

        Perfil perfil = perfilRepository.findById(request.idPerfil())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                String.format("Perfil com id %s não encontrado", request.idPerfil())));

        Usuario usuario = mapper.toDomain(request);
        usuario.setPerfil(perfil);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);

        return mapper.toResponse(usuario);
    }
}
