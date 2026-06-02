package com.nexa.auth.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.usecase.perfil.CadastrarPerfilUseCase;
import com.nexa.auth.domain.builder.perfil.PerfilBuilder;
import com.nexa.auth.domain.builder.usuario.UsuarioBuilder;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.domain.entity.perfil.TipoPerfil;
import com.nexa.auth.presentation.mapper.PerfilControllerMapper;
import com.nexa.auth.presentation.request.perfil.PerfilRequest;
import com.nexa.auth.presentation.request.usuario.UsuarioRequest;
import com.nexa.auth.presentation.response.perfil.PerfilResponse;
import com.nexa.auth.presentation.response.usuario.UsuarioResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PerfilController.class)
class PerfilControllerTest {

    private static final String BASE_URL = "/v1/perfis";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CadastrarPerfilUseCase cadastrarPerfilUseCase;

    @MockitoBean
    private PerfilControllerMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    private PerfilRequest request;
    private Perfil perfil;
    private PerfilResponse response;

    @BeforeEach
    void setUp() {

        request = new PerfilRequest(
                TipoPerfil.USER
        );

        perfil = new PerfilBuilder()
                .comId(1L)
                .comNome(TipoPerfil.USER)
                .build();

        response = new PerfilResponse(perfil.getId(), perfil.getNome());
    }

    @Test
    void deveCadastrarPerfil() throws Exception {
        when(mapper.toDomain(any())).thenReturn(perfil);
        when(cadastrarPerfilUseCase.cadastrarPerfil(any())).thenReturn(perfil);
        when(mapper.toResponse(any())).thenReturn(response);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_perfil").value(response.id()))
                .andExpect(jsonPath("$.nome").value(response.nome().name()));
    }

    @Test
    void deveRetornarErro400CasoPerfilJaExista() throws Exception {
        when(mapper.toDomain(any())).thenReturn(perfil);

        when(cadastrarPerfilUseCase.cadastrarPerfil(any()))
                .thenThrow(new BadRequestException("Este perfil já está cadastrado"));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Este perfil já está cadastrado"));
    }
}