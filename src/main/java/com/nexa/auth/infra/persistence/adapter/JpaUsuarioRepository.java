package com.nexa.auth.infra.persistence.adapter;

import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.UsuarioRepository;
import com.nexa.auth.infra.persistence.mapper.UsuarioPersistenceMapper;
import com.nexa.auth.infra.persistence.entity.UsuarioEntity;
import com.nexa.auth.infra.persistence.repository.SpringDataUsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class JpaUsuarioRepository implements UsuarioRepository {

    private final SpringDataUsuarioRepository usuarioRepository;
    private final UsuarioPersistenceMapper mapper;

    public JpaUsuarioRepository(SpringDataUsuarioRepository usuarioRepository, UsuarioPersistenceMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = mapper.toEntity(usuario);
        usuarioRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(mapper::toDomain);
    }
}
