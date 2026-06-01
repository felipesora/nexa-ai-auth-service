package com.nexa.auth.infra.persistence.repository;

import com.nexa.auth.infra.persistence.entity.PerfilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataPerfilRepository extends JpaRepository<PerfilEntity, Long> {
}
