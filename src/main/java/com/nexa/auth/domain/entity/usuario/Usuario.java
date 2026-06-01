package com.nexa.auth.domain.entity.usuario;

import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.exception.DomainException;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class Usuario {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private LocalDateTime criadoEm;
    private Boolean ativo;
    private Perfil perfil;

    public Usuario(Long id, String nome, String email, String senha, LocalDateTime criadoEm, Boolean ativo, Perfil perfil) {
        validarId(id);
        validarNome(nome);
        validarEmail(email);
        validarSenha(senha);
        validarPerfil(perfil);

        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.criadoEm = criadoEm;
        this.ativo = ativo;
        this.perfil = perfil;
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new DomainException("Id do usuário é obrigatório.");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DomainException("Nome é obrigatório.");
        }
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new DomainException("Email é obrigatório.");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new DomainException("Email inválido.");
        }
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new DomainException("Senha deve possuir no mínimo 6 caracteres.");
        }
    }

    private void validarPerfil(Perfil perfil) {
        if (perfil == null) {
            throw new DomainException("Perfil do usuário é obrigatório.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public Perfil getPerfil() {
        return perfil;
    }
}
