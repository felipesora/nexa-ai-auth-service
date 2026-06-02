package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.PerfilRepository;
import com.nexa.auth.domain.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class CadastrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;

    public CadastrarUsuarioUseCase(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
    }

    @Transactional
    public Usuario cadastrarUsuario(Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());

        if (usuarioExistente.isPresent()) {
            throw new BadRequestException("Este email já está cadastrado");
        }

        Perfil perfil = perfilRepository.findById(usuario.getPerfil().getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                String.format("Perfil com id %s não encontrado", usuario.getPerfil().getId())));

        usuario.setPerfil(perfil);

        return usuarioRepository.save(usuario);
    }
}
