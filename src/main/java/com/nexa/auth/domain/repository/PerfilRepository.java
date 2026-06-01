package com.nexa.auth.domain.repository;

import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.domain.entity.usuario.Usuario;

import java.util.List;
import java.util.Optional;

public interface PerfilRepository {

    Perfil save(Perfil perfil);

    List<Perfil> findAll();

    Optional<Perfil> findById(Long id);

    List<Usuario> findUsuariosByPerfil(TipoPerfil tipoPerfil);
}
