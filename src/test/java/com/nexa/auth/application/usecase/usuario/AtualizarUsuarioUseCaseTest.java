package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.domain.builder.perfil.PerfilBuilder;
import com.nexa.auth.domain.builder.usuario.UsuarioBuilder;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.PerfilRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AtualizarUsuarioUseCase useCase;

    @Test
    void deveAtualizarUsuario() {
        Long usuarioId = 1L;
        Long perfilId = 1L;

        Usuario usuarioExistente = new UsuarioBuilder().build();
        Perfil perfil = new PerfilBuilder()
                .comId(perfilId)
                .build();

        Usuario usuarioAtualizado = new UsuarioBuilder()
                .comNome("Felipe Atualizado")
                .comEmail("novo@email.com")
                .comSenha("123456atualizado")
                .comPerfil(perfil)
                .build();

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.findByEmail(usuarioAtualizado.getEmail())).thenReturn(Optional.empty());
        when(perfilRepository.findById(perfilId)).thenReturn(Optional.of(perfil));
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCriptografada");

        useCase.atualizarUsuario(usuarioId, usuarioAtualizado);

        assertEquals("Felipe Atualizado", usuarioExistente.getNome());
        assertEquals("novo@email.com", usuarioExistente.getEmail());
        assertEquals("senhaCriptografada", usuarioExistente.getSenha());
        assertEquals(perfil, usuarioExistente.getPerfil());

        verify(usuarioRepository).findById(usuarioId);
        verify(usuarioRepository).findByEmail("novo@email.com");
        verify(perfilRepository).findById(perfilId);
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    void deveLancarExcecaoUsuarioNaoEncontrado() {
        Long usuarioId = 1L;

        when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                        () -> useCase.atualizarUsuario(usuarioId, new UsuarioBuilder().build()));

        assertEquals("Usuário com id 1 não encontrado", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        Long usuarioId = 1L;

        Usuario usuarioExistente = new UsuarioBuilder()
                .comId(usuarioId)
                .build();

        Usuario usuarioAtualizado = new UsuarioBuilder()
                .comEmail("email@email.com")
                .build();

        Usuario outroUsuario = new UsuarioBuilder()
                .comId(2L)
                .comEmail("email@email.com")
                .build();

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioExistente));

        when(usuarioRepository.findByEmail("email@email.com")).thenReturn(Optional.of(outroUsuario));

        BadRequestException exception = assertThrows(BadRequestException.class,
                        () -> useCase.atualizarUsuario(usuarioId, usuarioAtualizado));

        assertEquals("Este email já está cadastrado", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoPerfilNaoEncontrado() {
        Long usuarioId = 1L;
        Long perfilId = 99L;

        Usuario usuarioExistente = new UsuarioBuilder()
                .comId(usuarioId)
                .build();

        Perfil perfil = new PerfilBuilder()
                .comId(perfilId)
                .build();

        Usuario usuarioAtualizado = new UsuarioBuilder()
                .comEmail("novo@email.com")
                .comPerfil(perfil)
                .build();

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioExistente));

        when(usuarioRepository.findByEmail("novo@email.com")).thenReturn(Optional.empty());

        when(perfilRepository.findById(perfilId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                        () -> useCase.atualizarUsuario(usuarioId, usuarioAtualizado));

        assertEquals("Perfil com id 99 não encontrado", exception.getMessage());
    }
}