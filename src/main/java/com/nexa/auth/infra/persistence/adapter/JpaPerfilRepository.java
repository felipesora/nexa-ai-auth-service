package com.nexa.auth.infra.persistence.adapter;

import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.PerfilRepository;
import com.nexa.auth.infra.mapper.PerfilPersistenceMapper;
import com.nexa.auth.infra.mapper.UsuarioPersistenceMapper;
import com.nexa.auth.infra.persistence.entity.PerfilEntity;
import com.nexa.auth.infra.persistence.repository.SpringDataPerfilRepository;
import com.nexa.auth.infra.persistence.repository.SpringDataUsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class JpaPerfilRepository implements PerfilRepository {

    private final SpringDataPerfilRepository perfilRepository;
    private final SpringDataUsuarioRepository usuarioRepository;
    private final PerfilPersistenceMapper perfilMapper;
    private final UsuarioPersistenceMapper usuarioMapper;

    public JpaPerfilRepository(SpringDataPerfilRepository perfilRepository, SpringDataUsuarioRepository usuarioRepository, PerfilPersistenceMapper perfilMapper, UsuarioPersistenceMapper usuarioMapper) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilMapper = perfilMapper;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public Perfil save(Perfil perfil) {
        PerfilEntity entity = perfilMapper.toEntity(perfil);
        perfilRepository.save(entity);
        return perfilMapper.toDomain(entity);
    }

    @Override
    public Page<Perfil> findAll(Pageable pageable) {
        return perfilRepository.findAll(pageable)
                .map(perfilMapper::toDomain);
    }

    @Override
    public Optional<Perfil> findById(Long id) {
        return perfilRepository.findById(id)
                .map(perfilMapper::toDomain);
    }

    @Override
    public Optional<Perfil> findByNome(TipoPerfil nome) {
        return perfilRepository.findByNome(nome)
                .map(perfilMapper::toDomain);
    }

    @Override
    public Page<Usuario> findUsuariosByPerfil(TipoPerfil nome, Pageable pageable) {
        return usuarioRepository.findUsuariosByPerfil(nome, pageable)
                .map(usuarioMapper::toDomain);
    }
}
