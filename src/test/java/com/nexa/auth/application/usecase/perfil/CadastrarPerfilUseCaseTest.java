package com.nexa.auth.application.usecase.perfil;

import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.domain.builder.perfil.PerfilBuilder;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.repository.PerfilRepository;
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
class CadastrarPerfilUseCaseTest {

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private CadastrarPerfilUseCase useCase;

    @Test
    void deveCadastrarPerfil() {
        Perfil perfil = new PerfilBuilder().build();

        when(perfilRepository.findByNome(perfil.getNome())).thenReturn(Optional.empty());
        when(perfilRepository.save(perfil)).thenReturn(perfil);

        var perfilCadastrado = useCase.cadastrarPerfil(perfil);

        assertNotNull(perfilCadastrado);
        assertEquals(perfil.getNome(), perfilCadastrado.getNome());
        verify(perfilRepository).save(perfil);
    }

    @Test
    void deveLancarExcecaoQuandoPerfilJaFoiCadastrado() {
        Perfil perfil = new PerfilBuilder().build();

        when(perfilRepository.findByNome(perfil.getNome())).thenReturn(Optional.of(perfil));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> useCase.cadastrarPerfil(perfil));

        assertEquals("Este perfil já está cadastrado", exception.getMessage());
        verify(perfilRepository, never()).save(any());
    }
}