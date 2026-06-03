package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.domain.builder.usuario.UsuarioBuilder;
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
class DesativarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DesativarUsuarioUseCase useCase;

    @Test
    void deveDesativarUsuario() {

        Long usuarioId = 1L;

        Usuario usuario = new UsuarioBuilder()
                .comId(usuarioId)
                .comAtivo(true)
                .build();

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        useCase.desativarUsuario(usuarioId);

        assertFalse(usuario.getAtivo());

        verify(usuarioRepository).findById(usuarioId);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {

        Long usuarioId = 1L;

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                        () -> useCase.desativarUsuario(usuarioId));

        assertEquals("Usuário com id 1 não encontrado", exception.getMessage());

        verify(usuarioRepository).findById(usuarioId);
        verify(usuarioRepository, never()).save(any());
    }
}