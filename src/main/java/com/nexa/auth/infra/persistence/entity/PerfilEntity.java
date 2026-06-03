package com.nexa.auth.infra.persistence.entity;

import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Perfis")
@NoArgsConstructor
@Getter
public class PerfilEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoPerfil nome;

    @OneToMany(mappedBy = "perfil")
    private List<UsuarioEntity> usuario;

    public PerfilEntity(Long id, TipoPerfil nome) {
        this.id = id;
        this.nome = nome;
    }
}
