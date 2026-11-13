package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.dto.usuario.AlterarSenhaRequest;
import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.domain.builder.usuario.UsuarioBuilder;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlterarSenhaUsuarioUseCaseTest {

    private static final String SENHA_CRIPTOGRAFADA = "senhaAtualCriptografada";

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AlterarSenhaUsuarioUseCase useCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioBuilder()
                .comId(1L)
                .build();
        usuario.setSenha(SENHA_CRIPTOGRAFADA);
    }

    @Test
    void deveAlterarSenhaQuandoSenhaAtualEstiverCorreta() {

        AlterarSenhaRequest request = new AlterarSenhaRequest("senhaAtual", "novaSenha123");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaAtual", SENHA_CRIPTOGRAFADA)).thenReturn(true);
        when(passwordEncoder.matches("novaSenha123", SENHA_CRIPTOGRAFADA)).thenReturn(false);
        when(passwordEncoder.encode("novaSenha123")).thenReturn("novaSenhaCriptografada");

        useCase.execute(1L, request);

        assertEquals("novaSenhaCriptografada", usuario.getSenha());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveLancarExcecaoQuandoSenhaAtualEstiverIncorreta() {

        AlterarSenhaRequest request = new AlterarSenhaRequest("senhaErrada", "novaSenha123");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaErrada", SENHA_CRIPTOGRAFADA)).thenReturn(false);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute(1L, request)
        );

        assertEquals("Senha atual incorreta", exception.getMessage());
        assertEquals(SENHA_CRIPTOGRAFADA, usuario.getSenha());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoNovaSenhaForIgualAAtual() {

        AlterarSenhaRequest request = new AlterarSenhaRequest("senhaAtual", "senhaAtual");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaAtual", SENHA_CRIPTOGRAFADA)).thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute(1L, request)
        );

        assertEquals("A nova senha deve ser diferente da senha atual", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {

        AlterarSenhaRequest request = new AlterarSenhaRequest("senhaAtual", "novaSenha123");

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(99L, request)
        );

        assertEquals("Usuário com id 99 não encontrado", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }
}
