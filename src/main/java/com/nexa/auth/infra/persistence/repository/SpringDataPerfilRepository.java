package com.nexa.auth.infra.persistence.repository;

import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.infra.persistence.entity.PerfilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataPerfilRepository extends JpaRepository<PerfilEntity, Long> {

    Optional<PerfilEntity> findByNome(TipoPerfil nome);
}
