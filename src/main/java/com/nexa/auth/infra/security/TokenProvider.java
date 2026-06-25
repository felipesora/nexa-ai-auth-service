package com.nexa.auth.infra.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenProvider {

    @Value("${jwt.expiration}")
    private Long expirationTime;

    @Value("${jwt.key}")
    private String key;

    // gerar token JWT
    public String gerarToken(Authentication authentication) {
        UserDetailsAdapter user = (UserDetailsAdapter) authentication.getPrincipal();
        return buildToken(
                user.getId(),
                user.getUsername(),
                user.getUsuario().getPerfil().getNome().name()
        );
    }

    private String buildToken(Long idUsuario, String username, String role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(username)
                .claim("idUsuario", idUsuario)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    // validar token JWT
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    // extrair informacoes do token
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        // validar assinatura do token
        // validar expiracao
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
