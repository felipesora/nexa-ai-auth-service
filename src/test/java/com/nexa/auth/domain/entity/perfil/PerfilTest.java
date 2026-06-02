package com.nexa.auth.domain.entity.perfil;

import com.nexa.auth.domain.builder.perfil.PerfilBuilder;
import com.nexa.auth.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerfilTest {

    @Test
    public void devePermitirCadastrarPerfilSeTodosOsAtributosEstiveremCorretos() {
        Perfil perfil = new PerfilBuilder().build();

        assertNotNull(perfil);
        assertEquals(1L, perfil.getId());
        assertEquals(TipoPerfil.USER, perfil.getNome());
    }

    @Test
    public void deveLancarDomainExceptionCasoNomeEstejaVazio() {
        DomainException exception = assertThrows(DomainException.class,
                () -> new PerfilBuilder().comNome(null).build());

        assertEquals("Nome do perfil é obrigatório.", exception.getMessage());
    }
}