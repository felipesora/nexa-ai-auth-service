package com.nexa.auth.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 255, name = "senha_hash")
    private String senha;

    @Column(nullable = false, name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(nullable = false)
    private Boolean ativo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_perfil", nullable = false)
    private PerfilEntity perfil;
}
