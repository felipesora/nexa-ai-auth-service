package com.nexa.auth.domain.repository;

import com.nexa.auth.domain.entity.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    Usuario save(Usuario usuario);

    Page<Usuario> findAll(Pageable pageable);

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByEmail(String email);
}
