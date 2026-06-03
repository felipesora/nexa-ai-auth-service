package com.nexa.auth.infra.persistence.repository;

import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.infra.persistence.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    @Query("""
        SELECT u
        FROM UsuarioEntity u
        WHERE u.perfil.nome = :nome
    """)
    Page<UsuarioEntity> findUsuariosByPerfil(@Param("nome") TipoPerfil nome, Pageable pageable);

    Optional<UsuarioEntity> findByEmail(String email);
}
