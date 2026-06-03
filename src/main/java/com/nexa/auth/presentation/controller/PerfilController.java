package com.nexa.auth.presentation.controller;

import com.nexa.auth.application.usecase.perfil.CadastrarPerfilUseCase;
import com.nexa.auth.application.usecase.perfil.ListarTodosPerfisUseCase;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.presentation.mapper.PerfilControllerMapper;
import com.nexa.auth.presentation.request.perfil.PerfilRequest;
import com.nexa.auth.presentation.response.perfil.PerfilResponse;
import com.nexa.auth.presentation.response.usuario.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/perfis")
public class PerfilController {

    private final CadastrarPerfilUseCase cadastrarPerfilUseCase;
    private final ListarTodosPerfisUseCase listarTodosPerfisUseCase;
    private final PerfilControllerMapper mapper;

    public PerfilController(CadastrarPerfilUseCase cadastrarPerfilUseCase,
                            ListarTodosPerfisUseCase listarTodosPerfisUseCase,
                            PerfilControllerMapper mapper) {
        this.cadastrarPerfilUseCase = cadastrarPerfilUseCase;
        this.listarTodosPerfisUseCase = listarTodosPerfisUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<PerfilResponse> cadastrarPerfil(@RequestBody @Valid PerfilRequest request,
                                                           UriComponentsBuilder uriBuilder) {
        Perfil perfil = mapper.toDomain(request);

        Perfil perfilSalvo = cadastrarPerfilUseCase.cadastrarPerfil(perfil);

        PerfilResponse response = mapper.toResponse(perfilSalvo);

        URI endereco = uriBuilder.path("/v1/perfis/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<PerfilResponse>> listarTodosPerfis(@PageableDefault(size = 10) Pageable pageable) {
        Page<PerfilResponse> perfis = listarTodosPerfisUseCase.listarTodosPerfis(pageable)
                .map(mapper::toResponse);

        return ResponseEntity.ok(perfis);
    }

}
