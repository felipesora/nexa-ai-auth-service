package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.exception.EntityNotFoundException;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private CadastrarUsuarioUseCase useCase;

    @Test
    void deveCadastrarUsuario() {
        Usuario usuario = new UsuarioBuilder().build();

        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());
        when(perfilRepository.findById(usuario.getPerfil().getId())).thenReturn(Optional.of(usuario.getPerfil()));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        var usuarioCadastrado = useCase.cadastrarUsuario(usuario);

        assertNotNull(usuarioCadastrado);
        assertEquals(usuario.getNome(), usuarioCadastrado.getNome());
        assertEquals(usuario.getEmail(), usuarioCadastrado.getEmail());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveAssociarPerfilEncontradoAoUsuario() {
        Usuario usuario = new UsuarioBuilder().build();

        Perfil perfilBanco = new Perfil(
                usuario.getPerfil().getId(),
                usuario.getPerfil().getNome()
        );

        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

        when(perfilRepository.findById(usuario.getPerfil().getId())).thenReturn(Optional.of(perfilBanco));

        when(usuarioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = useCase.cadastrarUsuario(usuario);

        assertSame(perfilBanco, resultado.getPerfil());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        Usuario usuario = new UsuarioBuilder().build();

        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> useCase.cadastrarUsuario(usuario));

        assertEquals("Este email já está cadastrado", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoPerfilNaoExiste() {
        Usuario usuario = new UsuarioBuilder().build();

        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

        when(perfilRepository.findById(usuario.getPerfil().getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> useCase.cadastrarUsuario(usuario));

        assertEquals(String.format("Perfil com id %s não encontrado", usuario.getPerfil().getId()),
                exception.getMessage());

        verify(usuarioRepository, never()).save(any());
    }
}