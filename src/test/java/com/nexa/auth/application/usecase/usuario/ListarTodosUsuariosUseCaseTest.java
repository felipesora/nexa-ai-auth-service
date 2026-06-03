package com.nexa.auth.application.usecase.usuario;

import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarTodosUsuariosUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ListarTodosUsuariosUseCase useCase;

    @Test
    void deveListarTodosUsuariosComPaginacao() {
        Pageable pageable = PageRequest.of(0, 10);

        Usuario usuario1 = mock(Usuario.class);
        Usuario usuario2 = mock(Usuario.class);

        Page<Usuario> paginaEsperada = new PageImpl<>(
                List.of(usuario1, usuario2),
                pageable,
                2
        );

        when(usuarioRepository.findAll(pageable))
                .thenReturn(paginaEsperada);

        Page<Usuario> resultado = useCase.listarTodosUsuarios(pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());

        verify(usuarioRepository).findAll(pageable);
    }
}