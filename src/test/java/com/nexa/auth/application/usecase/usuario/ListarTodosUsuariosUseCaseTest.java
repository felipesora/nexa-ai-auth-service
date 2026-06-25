package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.application.dto.usuario.UsuarioResponse;
import com.nexa.auth.application.mapper.UsuarioControllerMapper;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarTodosUsuariosUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioControllerMapper mapper;

    @InjectMocks
    private ListarTodosUsuariosUseCase useCase;

    @Test
    void deveListarTodosUsuariosComPaginacao() {

        Pageable pageable = PageRequest.of(0, 10);

        Usuario usuario1 = mock(Usuario.class);
        Usuario usuario2 = mock(Usuario.class);

        UsuarioResponse response1 = mock(UsuarioResponse.class);
        UsuarioResponse response2 = mock(UsuarioResponse.class);

        Page<Usuario> paginaUsuarios = new PageImpl<>(
                List.of(usuario1, usuario2),
                pageable,
                2
        );

        when(usuarioRepository.findAll(pageable))
                .thenReturn(paginaUsuarios);

        when(mapper.toResponse(usuario1))
                .thenReturn(response1);

        when(mapper.toResponse(usuario2))
                .thenReturn(response2);

        Page<UsuarioResponse> resultado = useCase.execute(pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());
        assertEquals(response1, resultado.getContent().get(0));
        assertEquals(response2, resultado.getContent().get(1));

        verify(usuarioRepository).findAll(pageable);
        verify(mapper).toResponse(usuario1);
        verify(mapper).toResponse(usuario2);
    }
}