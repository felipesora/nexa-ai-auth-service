package com.nexa.auth.application.usecase.auth;

import com.nexa.auth.application.dto.perfil.PerfilResponse;
import com.nexa.auth.application.dto.usuario.UsuarioRequest;
import com.nexa.auth.application.dto.usuario.UsuarioResponse;
import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.application.mapper.UsuarioControllerMapper;
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

import java.time.LocalDateTime;
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

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioControllerMapper mapper;

    @InjectMocks
    private CadastrarUsuarioUseCase useCase;

    @Test
    void deveCadastrarUsuario() {

        Usuario usuario = new UsuarioBuilder().build();

        UsuarioRequest request = new UsuarioRequest(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getPerfil().getId()
        );

        UsuarioResponse response = new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                LocalDateTime.now(),
                usuario.getAtivo(),
                new PerfilResponse(
                        usuario.getPerfil().getId(),
                        usuario.getPerfil().getNome()
                )
        );

        when(usuarioRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        when(perfilRepository.findById(request.idPerfil()))
                .thenReturn(Optional.of(usuario.getPerfil()));

        when(mapper.toDomain(request))
                .thenReturn(usuario);

        when(passwordEncoder.encode(usuario.getSenha()))
                .thenReturn("senhaCriptografada");

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        when(mapper.toResponse(usuario))
                .thenReturn(response);

        UsuarioResponse usuarioCadastrado = useCase.execute(request);

        assertNotNull(usuarioCadastrado);
        assertEquals(usuario.getNome(), usuarioCadastrado.nome());
        assertEquals(usuario.getEmail(), usuarioCadastrado.email());
        assertEquals("senhaCriptografada", usuario.getSenha());

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveAssociarPerfilEncontradoAoUsuario() {

        Usuario usuario = new UsuarioBuilder().build();

        UsuarioRequest request = new UsuarioRequest(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getPerfil().getId()
        );

        Perfil perfilBanco = new Perfil(
                usuario.getPerfil().getId(),
                usuario.getPerfil().getNome()
        );

        UsuarioResponse response = new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                LocalDateTime.now(),
                usuario.getAtivo(),
                new PerfilResponse(
                        perfilBanco.getId(),
                        perfilBanco.getNome()
                )
        );

        when(usuarioRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        when(perfilRepository.findById(request.idPerfil()))
                .thenReturn(Optional.of(perfilBanco));

        when(mapper.toDomain(request))
                .thenReturn(usuario);

        when(passwordEncoder.encode(anyString()))
                .thenReturn("senha");

        when(usuarioRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(mapper.toResponse(usuario))
                .thenReturn(response);

        useCase.execute(request);

        assertSame(perfilBanco, usuario.getPerfil());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {

        Usuario usuario = new UsuarioBuilder().build();

        UsuarioRequest request = new UsuarioRequest(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getPerfil().getId()
        );

        when(usuarioRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(usuario));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute(request)
        );

        assertEquals("Este email já está cadastrado", exception.getMessage());

        verify(usuarioRepository, never()).save(any());
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void deveLancarExcecaoQuandoPerfilNaoExiste() {

        Usuario usuario = new UsuarioBuilder().build();

        UsuarioRequest request = new UsuarioRequest(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getPerfil().getId()
        );

        when(usuarioRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        when(perfilRepository.findById(request.idPerfil()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(request)
        );

        assertEquals(
                String.format("Perfil com id %s não encontrado", request.idPerfil()),
                exception.getMessage()
        );

        verify(usuarioRepository, never()).save(any());
        verify(mapper, never()).toDomain(any());
    }
}