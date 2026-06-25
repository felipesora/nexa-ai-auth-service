package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.dto.usuario.UsuarioResponse;
import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.mapper.UsuarioControllerMapper;
import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.PerfilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarUsuariosPorPerfilUseCaseTest {

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private UsuarioControllerMapper mapper;

    private ListarUsuariosPorPerfilUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListarUsuariosPorPerfilUseCase(perfilRepository, mapper);
    }

    @Test
    void deveListarUsuariosPorPerfil() {

        Pageable pageable = PageRequest.of(0, 10);

        Usuario usuario = mock(Usuario.class);
        UsuarioResponse response = mock(UsuarioResponse.class);

        Page<Usuario> paginaUsuarios = new PageImpl<>(
                List.of(usuario),
                pageable,
                1
        );

        when(perfilRepository.findUsuariosByPerfil(TipoPerfil.USER, pageable))
                .thenReturn(paginaUsuarios);

        when(mapper.toResponse(usuario))
                .thenReturn(response);

        Page<UsuarioResponse> resultado =
                useCase.execute("user", pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(1, resultado.getContent().size());
        assertEquals(response, resultado.getContent().getFirst());

        verify(perfilRepository)
                .findUsuariosByPerfil(TipoPerfil.USER, pageable);

        verify(mapper).toResponse(usuario);
        verifyNoMoreInteractions(perfilRepository);
    }

    @Test
    void deveLancarExcecaoQuandoPerfilForInvalido() {

        Pageable pageable = PageRequest.of(0, 10);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute("perfil_inexistente", pageable)
        );

        assertEquals(
                "Perfil 'perfil_inexistente' é inválido",
                exception.getMessage()
        );

        verifyNoInteractions(perfilRepository);
        verifyNoInteractions(mapper);
    }
}