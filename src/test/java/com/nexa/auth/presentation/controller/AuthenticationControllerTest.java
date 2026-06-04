package com.nexa.auth.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.auth.application.dto.auth.LoginRequestDto;
import com.nexa.auth.application.dto.auth.TokenResponseDto;
import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.application.usecase.auth.CadastrarUsuarioUseCase;
import com.nexa.auth.application.usecase.auth.RealizarLoginUseCase;
import com.nexa.auth.domain.builder.perfil.PerfilBuilder;
import com.nexa.auth.domain.builder.usuario.UsuarioBuilder;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.infra.security.JwtAuthenticationFilter;
import com.nexa.auth.infra.security.TokenProvider;
import com.nexa.auth.presentation.mapper.UsuarioControllerMapper;
import com.nexa.auth.presentation.request.usuario.UsuarioRequest;
import com.nexa.auth.presentation.response.perfil.PerfilResponse;
import com.nexa.auth.presentation.response.usuario.UsuarioResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    private static final String BASE_URL = "/v1/auth";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;

    @MockitoBean
    private RealizarLoginUseCase realizarLoginUseCase;

    @MockitoBean
    private UsuarioControllerMapper mapper;

    @MockitoBean
    private TokenProvider tokenProvider;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioRequest request;
    private Usuario usuario;
    private UsuarioResponse response;

    @BeforeEach
    void setUp() {

        request = new UsuarioRequest(
                "Felipe",
                "felipe@email.com",
                "felipe10",
                1L
        );

        Perfil perfil = new PerfilBuilder()
                .comId(1L)
                .comNome(TipoPerfil.USER)
                .build();

        usuario = new UsuarioBuilder()
                .comId(1L)
                .comNome("Felipe")
                .comEmail("felipe@email.com")
                .comSenha("felipe10")
                .comPerfil(perfil)
                .build();

        PerfilResponse perfilResponse = new PerfilResponse(perfil.getId(), perfil.getNome());

        response = new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCriadoEm(),
                usuario.getAtivo(),
                perfilResponse
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveCadastrarUsuario() throws Exception {

        when(mapper.toDomain(any())).thenReturn(usuario);
        when(cadastrarUsuarioUseCase.cadastrarUsuario(any())).thenReturn(usuario);
        when(mapper.toResponse(any())).thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(response.nome()))
                .andExpect(jsonPath("$.email").value(response.email()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarErro400CasoEmailJaEstejaCadastradoAoCadastrarUsuario() throws Exception {
        when(mapper.toDomain(any())).thenReturn(usuario);

        when(cadastrarUsuarioUseCase.cadastrarUsuario(any()))
                .thenThrow(new BadRequestException("Este email já está cadastrado"));

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Este email já está cadastrado"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarErro404CasoIdDoPerfilNaoExistaAoCadastrarUsuario() throws Exception {
        when(mapper.toDomain(any())).thenReturn(usuario);

        when(cadastrarUsuarioUseCase.cadastrarUsuario(any()))
                .thenThrow(new EntityNotFoundException(
                        String.format("Perfil com id %s não encontrado", usuario.getPerfil().getId())));

        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value(String.format("Perfil com id %s não encontrado", usuario.getPerfil().getId())));
    }

    @Test
    void deveRealizarLogin() throws Exception {

        LoginRequestDto request = new LoginRequestDto(
                "felipe@email.com",
                "123456"
        );

        TokenResponseDto response = new TokenResponseDto(
                "jwt-token-teste",
                3600L
        );

        when(realizarLoginUseCase.fazerLogin(any(LoginRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-teste"))
                .andExpect(jsonPath("$.expiresIn").value(3600));
    }

    @Test
    void deveRetornar400QuandoEmailForVazio() throws Exception {

        LoginRequestDto request = new LoginRequestDto(
                "",
                "123456"
        );

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoSenhaForVazia() throws Exception {

        LoginRequestDto request = new LoginRequestDto(
                "felipe@email.com",
                ""
        );

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar401QuandoCredenciaisForemInvalidas() throws Exception {

        LoginRequestDto request = new LoginRequestDto(
                "felipe@email.com",
                "senhaErrada"
        );

        when(realizarLoginUseCase.fazerLogin(any(LoginRequestDto.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Credenciais inválidas"));
    }
}