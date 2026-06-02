package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarUsuarioPorIdUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private BuscarUsuarioPorIdUseCase useCase;

    @Test
    void deveBuscarUsuarioPorId() {
        // Arrange
        Long id = 1L;
        Usuario usuario = mock(Usuario.class);

        when(usuarioRepository.findById(id))
                .thenReturn(Optional.of(usuario));

        // Act
        Usuario resultado = useCase.buscarUsuarioPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(usuario, resultado);

        verify(usuarioRepository).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        Long id = 1L;

        when(usuarioRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.buscarUsuarioPorId(id)
        );

        assertEquals(
                String.format("Usuário com id %s não encontrado", id),
                exception.getMessage()
        );

        verify(usuarioRepository).findById(id);
    }
}