package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.dto.usuario.UsuarioResponse;
import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.application.mapper.UsuarioControllerMapper;
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

    @Mock
    private UsuarioControllerMapper mapper;

    @InjectMocks
    private BuscarUsuarioPorIdUseCase useCase;

    @Test
    void deveBuscarUsuarioPorId() {

        Long id = 1L;

        Usuario usuario = mock(Usuario.class);
        UsuarioResponse response = mock(UsuarioResponse.class);

        when(usuarioRepository.findById(id))
                .thenReturn(Optional.of(usuario));

        when(mapper.toResponse(usuario))
                .thenReturn(response);

        UsuarioResponse resultado = useCase.execute(id);

        assertNotNull(resultado);
        assertEquals(response, resultado);

        verify(usuarioRepository).findById(id);
        verify(mapper).toResponse(usuario);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {

        Long id = 1L;

        when(usuarioRepository.findById(id))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(id)
        );

        assertEquals(
                String.format("Usuário com id %s não encontrado", id),
                exception.getMessage()
        );

        verify(usuarioRepository).findById(id);
        verifyNoInteractions(mapper);
    }
}