package com.nexa.auth.infra.persistence.entity;

import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Perfis")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PerfilEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoPerfil nome;

    @OneToMany(mappedBy = "perfil")
    private List<UsuarioEntity> usuario;
}
