package com.nexa.auth.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.auth.application.exception.BadRequestException;
import com.nexa.auth.application.exception.EntityNotFoundException;
import com.nexa.auth.application.usecase.auth.CadastrarUsuarioUseCase;
import com.nexa.auth.application.usecase.usuario.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    private static final String BASE_URL = "/v1/usuarios";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;

    @MockitoBean
    private ListarTodosUsuariosUseCase listarTodosUsuariosUseCase;

    @MockitoBean
    private ListarUsuariosPorPerfilUseCase listarUsuariosPorPerfilUseCase;

    @MockitoBean
    private BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;

    @MockitoBean
    private AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    @MockitoBean
    private DesativarUsuarioUseCase desativarUsuarioUseCase;

    @MockitoBean
    private AtivarUsuarioUseCase ativarUsuarioUseCase;

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
    void deveRetornarUmaListaDeUsuariosPaginada() throws Exception {

        Page<Usuario> page = new PageImpl<>(
                List.of(usuario),
                PageRequest.of(0, 10),
                1
        );

        when(listarTodosUsuariosUseCase.listarTodosUsuarios(any()))
                .thenReturn(page);

        when(mapper.toResponse(usuario))
                .thenReturn(response);

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_usuario").value(response.id()))
                .andExpect(jsonPath("$.content[0].nome").value(response.nome()))
                .andExpect(jsonPath("$.content[0].email").value(response.email()))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarPaginaVazia() throws Exception {

        when(listarTodosUsuariosUseCase.listarTodosUsuarios(any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarUmaListaDeUsuariosPorPerfilPaginada() throws Exception {

        Page<Usuario> page = new PageImpl<>(
                List.of(usuario),
                PageRequest.of(0, 10),
                1
        );

        when(listarUsuariosPorPerfilUseCase.listarUsuariosPorPerfil(
                eq("USER"),
                any(Pageable.class)))
                .thenReturn(page);

        when(mapper.toResponse(usuario)).thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/perfil")
                        .param("nomePerfil", "USER")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_usuario").value(response.id()))
                .andExpect(jsonPath("$.content[0].nome").value(response.nome()))
                .andExpect(jsonPath("$.content[0].email").value(response.email()))
                .andExpect(jsonPath("$.content[0].perfil.nome").value("USER"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveBuscarUsuarioPorId() throws Exception {
        when(buscarUsuarioPorIdUseCase.buscarUsuarioPorId(any()))
                .thenReturn(usuario);

        when(mapper.toResponse(usuario))
                .thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_usuario").value(response.id()))
                .andExpect(jsonPath("$.nome").value(response.nome()))
                .andExpect(jsonPath("$.email").value(response.email()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarErro404CasoUsuarioNaoEncontrado() throws Exception {
        when(buscarUsuarioPorIdUseCase.buscarUsuarioPorId(any()))
                .thenThrow(new EntityNotFoundException(
                        String.format("Usuário com id %s não encontrado", usuario.getId())));

        mockMvc.perform(get(BASE_URL + "/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value(String.format("Usuário com id %s não encontrado", usuario.getId())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarUsuario() throws Exception {
        when(mapper.toDomain(any()))
                .thenReturn(usuario);

        mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarErro404CasoUsuarioNaoEncontradoAoAtualizar() throws Exception {
        when(mapper.toDomain(any()))
                .thenReturn(usuario);

        doThrow(new EntityNotFoundException(String.format("Usuário com id %s não encontrado", usuario.getPerfil().getId())))
                .when(atualizarUsuarioUseCase)
                .atualizarUsuario(any(), any());

        mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value(String.format("Usuário com id %s não encontrado", usuario.getId())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarErro404CasoPerfilNaoEncontradoAoAtualizar() throws Exception {
        when(mapper.toDomain(any()))
                .thenReturn(usuario);

        doThrow(new EntityNotFoundException(String.format("Perfil com id %s não encontrado", usuario.getPerfil().getId())))
                .when(atualizarUsuarioUseCase)
                .atualizarUsuario(any(), any());

        mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value(String.format("Perfil com id %s não encontrado",
                                usuario.getPerfil().getId())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarErro400CasoEmailJaCadastradoAoAtualizar() throws Exception {
        when(mapper.toDomain(any()))
                .thenReturn(usuario);

        doThrow(new BadRequestException("Este email já está cadastrado"))
                .when(atualizarUsuarioUseCase)
                .atualizarUsuario(any(), any());

        mockMvc.perform(put(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Este email já está cadastrado"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveDesativarUsuario() throws Exception {

        mockMvc.perform(delete(BASE_URL + "/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarErro404CasoUsuarioNaoEncontradoAoDesativar() throws Exception {

        doThrow(new EntityNotFoundException(
                String.format("Usuário com id %s não encontrado", usuario.getId())))
                .when(desativarUsuarioUseCase)
                .desativarUsuario(any());

        mockMvc.perform(delete(BASE_URL + "/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value(String.format(
                                "Usuário com id %s não encontrado",
                                usuario.getId())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveAtivarUsuario() throws Exception {

        mockMvc.perform(patch(BASE_URL + "/ativar/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornarErro404CasoUsuarioNaoEncontradoAoAtivar() throws Exception {

        doThrow(new EntityNotFoundException(
                String.format("Usuário com id %s não encontrado", usuario.getId())))
                .when(ativarUsuarioUseCase)
                .ativarUsuario(any());

        mockMvc.perform(patch(BASE_URL + "/ativar/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value(String.format(
                                "Usuário com id %s não encontrado",
                                usuario.getId())));
    }
}