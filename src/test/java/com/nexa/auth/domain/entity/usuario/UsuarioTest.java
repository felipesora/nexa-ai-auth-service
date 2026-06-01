package com.nexa.auth.domain.entity.usuario;

import com.nexa.auth.domain.builder.usuario.UsuarioBuilder;
import com.nexa.auth.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    public void devePermitirCadastrarUsuarioSeTodosOsAtributosEstiveremCorretos() {
        Usuario usuario = new UsuarioBuilder().build();

        assertNotNull(usuario);
        assertEquals(1L, usuario.getId());
        assertEquals("Felipe", usuario.getNome());
        assertEquals("felipe@email.com", usuario.getEmail());
        assertEquals("123456", usuario.getSenha());
        assertNotNull(usuario.getCriadoEm());
        assertTrue(usuario.getAtivo());
    }

    @Test
    public void deveLancarDomainExceptionCasoNomeEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new UsuarioBuilder().comNome("").build());

        assertEquals("Nome é obrigatório.", exception.getMessage());
    }

    @Test
    public void deveLancarDomainExceptionCasoEmailEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new UsuarioBuilder().comEmail("").build());

        assertEquals("Email é obrigatório.", exception.getMessage());
    }

    @Test
    public void deveLancarDomainExceptionCasoEmailEstejaNoFormatoInvalido() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new UsuarioBuilder().comEmail("email.com").build());

        assertEquals("Email inválido.", exception.getMessage());
    }

    @Test
    public void deveLancarDomainExceptionCasoSenhaEstejaNoFormatoInvalido() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new UsuarioBuilder().comSenha("").build());


        assertEquals("Senha deve possuir no mínimo 6 caracteres.", exception.getMessage());
    }
}