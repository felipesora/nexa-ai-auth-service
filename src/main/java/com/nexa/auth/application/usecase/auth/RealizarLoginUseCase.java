package com.nexa.auth.application.usecase.auth;

import com.nexa.auth.application.dto.auth.LoginRequestDto;
import com.nexa.auth.application.dto.auth.TokenResponseDto;
import com.nexa.auth.infra.security.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class RealizarLoginUseCase {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public RealizarLoginUseCase(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public TokenResponseDto fazerLogin(LoginRequestDto dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()));
            String token = tokenProvider.gerarToken(authentication);

            return new TokenResponseDto(token, expirationTime);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciais inválidas");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
