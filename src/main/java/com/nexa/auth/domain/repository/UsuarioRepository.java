package com.nexa.auth.domain.repository;

import com.nexa.auth.domain.entity.usuario.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    Usuario save(Usuario usuario);

    List<Usuario> findAll();

    Optional<Usuario> findById(Long id);

    Usuario update(Long id, Usuario usuario);

    void delete(Long id);
}
