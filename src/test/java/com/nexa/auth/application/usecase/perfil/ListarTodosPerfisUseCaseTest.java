package com.nexa.auth.application.usecase.perfil;

import com.nexa.auth.application.dto.perfil.PerfilResponse;
import com.nexa.auth.application.mapper.PerfilControllerMapper;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.repository.PerfilRepository;
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
class ListarTodosPerfisUseCaseTest {

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private PerfilControllerMapper mapper;

    @InjectMocks
    private ListarTodosPerfisUseCase useCase;

    @Test
    void deveListarTodosPerfisComPaginacao() {
        Pageable pageable = PageRequest.of(0, 10);

        Perfil perfil1 = mock(Perfil.class);
        Perfil perfil2 = mock(Perfil.class);

        PerfilResponse response1 = mock(PerfilResponse.class);
        PerfilResponse response2 = mock(PerfilResponse.class);

        Page<Perfil> paginaPerfis = new PageImpl<>(
                List.of(perfil1, perfil2),
                pageable,
                2
        );

        when(perfilRepository.findAll(pageable)).thenReturn(paginaPerfis);
        when(mapper.toResponse(perfil1)).thenReturn(response1);
        when(mapper.toResponse(perfil2)).thenReturn(response2);

        Page<PerfilResponse> resultado = useCase.execute(pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());
        assertEquals(response1, resultado.getContent().get(0));
        assertEquals(response2, resultado.getContent().get(1));

        verify(perfilRepository).findAll(pageable);
        verify(mapper).toResponse(perfil1);
        verify(mapper).toResponse(perfil2);
    }
}