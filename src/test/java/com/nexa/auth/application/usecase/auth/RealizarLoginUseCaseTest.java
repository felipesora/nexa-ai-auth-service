package com.nexa.auth.application.usecase.auth;

import com.nexa.auth.application.dto.auth.LoginRequestDto;
import com.nexa.auth.application.dto.auth.TokenResponseDto;
import com.nexa.auth.infra.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RealizarLoginUseCaseTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private RealizarLoginUseCase useCase;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(useCase, "expirationTime", 3600L);
    }

    @Test
    void deveRealizarLoginComSucesso() {

        LoginRequestDto dto = new LoginRequestDto(
                "felipe@email.com",
                "123456"
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(tokenProvider.gerarToken(authentication))
                .thenReturn("jwt-token");

        TokenResponseDto response = useCase.fazerLogin(dto);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        assertEquals(3600L, response.expiresIn());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).gerarToken(authentication);
    }

    @Test
    void deveLancarBadCredentialsExceptionQuandoCredenciaisForemInvalidas() {

        LoginRequestDto dto = new LoginRequestDto(
                "felipe@email.com",
                "senhaErrada"
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> useCase.fazerLogin(dto)
        );

        assertEquals("Credenciais inválidas", exception.getMessage());

        verify(tokenProvider, never()).gerarToken(any());
    }

    @Test
    void deveLancarRuntimeExceptionQuandoOcorrerErroInesperado() {

        LoginRequestDto dto = new LoginRequestDto(
                "felipe@email.com",
                "123456"
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Erro inesperado"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> useCase.fazerLogin(dto)
        );

        assertNotNull(exception.getCause());
        assertEquals("Erro inesperado", exception.getCause().getMessage());

        verify(tokenProvider, never()).gerarToken(any());
    }
}