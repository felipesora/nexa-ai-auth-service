package com.nexa.auth.domain.repository;

import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.domain.entity.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PerfilRepository {

    Perfil save(Perfil perfil);

    Page<Perfil> findAll(Pageable pageable);

    Optional<Perfil> findById(Long id);

    Optional<Perfil> findByNome(TipoPerfil nome);

    Page<Usuario> findUsuariosByPerfil(TipoPerfil nome, Pageable pageable);
}
