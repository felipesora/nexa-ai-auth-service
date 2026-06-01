package com.nexa.auth.infra.persistence.repository;

import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.infra.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    @Query("""
        SELECT u
        FROM UsuarioEntity u
        WHERE u.perfil.nome = :nome
    """)
    List<UsuarioEntity> findUsuariosByPerfil(@Param("nome") TipoPerfil nome);
}
