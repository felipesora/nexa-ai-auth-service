package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.dto.usuario.UsuarioRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AtualizarUsuarioUseCase useCase;

    @Test
    void deveAtualizarUsuario() {

        Long usuarioId = 1L;
        Usuario usuarioExistente = new UsuarioBuilder()
                .comId(usuarioId)
                .build();

        UsuarioRequest request = new UsuarioRequest(
                "Felipe Atualizado",
                "novo@email.com",
                "123456atualizado"
        );

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuarioExistente));

        when(usuarioRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(anyString()))
                .thenReturn("senhaCriptografada");

        useCase.execute(usuarioId, request);

        assertEquals(request.nome(), usuarioExistente.getNome());
        assertEquals(request.email(), usuarioExistente.getEmail());
        assertEquals("senhaCriptografada", usuarioExistente.getSenha());

        verify(usuarioRepository).findById(usuarioId);
        verify(usuarioRepository).findByEmail(request.email());
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    void deveLancarExcecaoUsuarioNaoEncontrado() {

        Long usuarioId = 1L;

        UsuarioRequest request = new UsuarioRequest(
                "Felipe",
                "felipe@email.com",
                "123456"
        );

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(usuarioId, request)
        );

        assertEquals("Usuário com id 1 não encontrado", exception.getMessage());
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

        UsuarioRequest request = new UsuarioRequest(
                "Felipe",
                "email@email.com",
                "123456"
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
    }
}
