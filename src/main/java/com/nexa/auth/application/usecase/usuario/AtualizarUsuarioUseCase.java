package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.PerfilRepository;
import com.nexa.auth.domain.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class AtualizarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;

    public AtualizarUsuarioUseCase(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
    }

    @Transactional
    public void atualizarUsuario(Long id, Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Usuário com id %s não encontrado", id)));

        Optional<Usuario> emailExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (emailExistente.isPresent() && !emailExistente.get().getId().equals(id)) {
            throw new BadRequestException("Este email já está cadastrado");
        }

        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setSenha(usuario.getSenha());

        if (usuario.getPerfil() != null) {
            var perfil = perfilRepository.findById(usuario.getPerfil().getId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Perfil com id %s não encontrado", usuario.getPerfil().getId())));
            usuarioExistente.setPerfil(perfil);
        }

        usuarioRepository.save(usuarioExistente);
    }
}
