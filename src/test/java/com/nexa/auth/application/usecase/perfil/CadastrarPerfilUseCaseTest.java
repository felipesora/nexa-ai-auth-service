package com.nexa.auth.application.usecase.perfil;

import com.nexa.auth.application.dto.perfil.PerfilRequest;
import com.nexa.auth.application.dto.perfil.PerfilResponse;
import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.mapper.PerfilControllerMapper;
import com.nexa.auth.domain.builder.perfil.PerfilBuilder;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.domain.repository.PerfilRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarPerfilUseCaseTest {

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private PerfilControllerMapper mapper;

    @InjectMocks
    private CadastrarPerfilUseCase useCase;

    @Test
    void deveCadastrarPerfil() {
        PerfilRequest request = new PerfilRequest(TipoPerfil.ADMIN);
        Perfil perfil = new PerfilBuilder()
                .comNome(TipoPerfil.ADMIN)
                .build();

        PerfilResponse response = new PerfilResponse(
                perfil.getId(),
                perfil.getNome()
        );

        when(perfilRepository.findByNome(request.nome())).thenReturn(Optional.empty());
        when(mapper.toDomain(request)).thenReturn(perfil);
        when(perfilRepository.save(perfil)).thenReturn(perfil);
        when(mapper.toResponse(perfil)).thenReturn(response);

        PerfilResponse perfilCadastrado = useCase.execute(request);

        assertNotNull(perfilCadastrado);
        assertEquals(request.nome(), perfilCadastrado.nome());

        verify(perfilRepository).findByNome(request.nome());
        verify(mapper).toDomain(request);
        verify(perfilRepository).save(perfil);
        verify(mapper).toResponse(perfil);
    }

    @Test
    void deveLancarExcecaoQuandoPerfilJaFoiCadastrado() {
        PerfilRequest request = new PerfilRequest(TipoPerfil.ADMIN);

        Perfil perfil = new PerfilBuilder()
                .comNome(TipoPerfil.ADMIN)
                .build();

        when(perfilRepository.findByNome(request.nome()))
                .thenReturn(Optional.of(perfil));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute(request)
        );

        assertEquals("Este perfil já está cadastrado", exception.getMessage());

        verify(perfilRepository).findByNome(request.nome());
        verify(perfilRepository, never()).save(any());
        verify(mapper, never()).toDomain(any());
        verify(mapper, never()).toResponse(any());
    }
}