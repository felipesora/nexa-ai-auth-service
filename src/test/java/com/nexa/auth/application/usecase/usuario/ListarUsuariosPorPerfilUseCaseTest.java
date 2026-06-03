package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.PerfilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarUsuariosPorPerfilUseCaseTest {

    @Mock
    private PerfilRepository perfilRepository;

    private ListarUsuariosPorPerfilUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListarUsuariosPorPerfilUseCase(perfilRepository);
    }

    @Test
    void deveListarUsuariosPorPerfil() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        Usuario usuario = mock(Usuario.class);

        Page<Usuario> pageEsperada =
                new PageImpl<>(java.util.List.of(usuario));

        when(perfilRepository.findUsuariosByPerfil(
                TipoPerfil.USER,
                pageable))
                .thenReturn(pageEsperada);

        // Act
        Page<Usuario> resultado =
                useCase.listarUsuariosPorPerfil("user", pageable);

        // Assert
        assertEquals(pageEsperada, resultado);

        verify(perfilRepository)
                .findUsuariosByPerfil(TipoPerfil.USER, pageable);

        verifyNoMoreInteractions(perfilRepository);
    }
}