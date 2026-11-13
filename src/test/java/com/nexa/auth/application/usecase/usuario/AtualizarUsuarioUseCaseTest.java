package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.dto.usuario.UsuarioUpdateRequest;
import com.nexa.auth.application.exception.BadRequestException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AtualizarUsuarioUseCase useCase;

    @Test
    void deveAtualizarNomeEEmailSemAlterarSenha() {

        Long usuarioId = 1L;
        Usuario usuarioExistente = new UsuarioBuilder()
                .comId(usuarioId)
                .build();
        String senhaOriginal = usuarioExistente.getSenha();

        UsuarioUpdateRequest request = new UsuarioUpdateRequest(
                "Felipe Atualizado",
                "novo@email.com"
        );

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuarioExistente));

        when(usuarioRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        useCase.execute(usuarioId, request);

        assertEquals(request.nome(), usuarioExistente.getNome());
        assertEquals(request.email(), usuarioExistente.getEmail());
        assertEquals(senhaOriginal, usuarioExistente.getSenha());

        verify(usuarioRepository).findById(usuarioId);
        verify(usuarioRepository).findByEmail(request.email());
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    void devePermitirManterOProprioEmail() {

        Long usuarioId = 1L;
        Usuario usuarioExistente = new UsuarioBuilder()
                .comId(usuarioId)
                .comEmail("felipe@email.com")
                .build();

        UsuarioUpdateRequest request = new UsuarioUpdateRequest(
                "Felipe Atualizado",
                "felipe@email.com"
        );

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuarioExistente));

        when(usuarioRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(usuarioExistente));

        useCase.execute(usuarioId, request);

        assertEquals("Felipe Atualizado", usuarioExistente.getNome());
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    void deveLancarExcecaoUsuarioNaoEncontrado() {

        Long usuarioId = 1L;

        UsuarioUpdateRequest request = new UsuarioUpdateRequest(
                "Felipe",
                "felipe@email.com"
        );

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(usuarioId, request)
        );

        assertEquals("Usuário com id 1 não encontrado", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {

        Long usuarioId = 1L;

        Usuario usuarioExistente = new UsuarioBuilder()
                .comId(usuarioId)
                .build();

        Usuario outroUsuario = new UsuarioBuilder()
                .comId(2L)
                .comEmail("email@email.com")
                .build();

        UsuarioUpdateRequest request = new UsuarioUpdateRequest(
                "Felipe",
                "email@email.com"
        );

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuarioExistente));

        when(usuarioRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(outroUsuario));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute(usuarioId, request)
        );

        assertEquals("Este email já está cadastrado", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }
}
