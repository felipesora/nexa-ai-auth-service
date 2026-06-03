package com.nexa.auth.application.usecase.perfil;

import com.nexa.auth.application.usecase.usuario.ListarTodosUsuariosUseCase;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.domain.repository.PerfilRepository;
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
class ListarTodosPerfisUseCaseTest {

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private ListarTodosPerfisUseCase useCase;

    @Test
    void deveListarTodosPerfisComPaginacao() {
        Pageable pageable = PageRequest.of(0, 10);

        Perfil perfil1 = mock(Perfil.class);
        Perfil perfil2 = mock(Perfil.class);

        Page<Perfil> paginaEsperada = new PageImpl<>(
                List.of(perfil1, perfil2),
                pageable,
                2
        );

        when(perfilRepository.findAll(pageable))
                .thenReturn(paginaEsperada);

        Page<Perfil> resultado = useCase.listarTodosPerfis(pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());

        verify(perfilRepository).findAll(pageable);
    }
}