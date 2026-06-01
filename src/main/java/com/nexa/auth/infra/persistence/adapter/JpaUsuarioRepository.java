package com.nexa.auth.infra.persistence.adapter;

import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.UsuarioRepository;
import com.nexa.auth.infra.mapper.UsuarioPersistenceMapper;
import com.nexa.auth.infra.persistence.entity.UsuarioEntity;
import com.nexa.auth.infra.persistence.repository.SpringDataUsuarioRepository;

import java.util.List;
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
    public List<Usuario> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
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
