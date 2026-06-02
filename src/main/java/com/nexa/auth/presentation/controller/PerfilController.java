package com.nexa.auth.presentation.controller;

import com.nexa.auth.application.usecase.perfil.CadastrarPerfilUseCase;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.presentation.mapper.PerfilControllerMapper;
import com.nexa.auth.presentation.request.perfil.PerfilRequest;
import com.nexa.auth.presentation.response.perfil.PerfilResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/perfis")
public class PerfilController {

    private final CadastrarPerfilUseCase cadastrarPerfilUseCase;
    private final PerfilControllerMapper mapper;

    public PerfilController(CadastrarPerfilUseCase cadastrarPerfilUseCase, PerfilControllerMapper mapper) {
        this.cadastrarPerfilUseCase = cadastrarPerfilUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<PerfilResponse> cadastrarUsuario(@RequestBody @Valid PerfilRequest request,
                                                           UriComponentsBuilder uriBuilder) {
        Perfil perfil = mapper.toDomain(request);

        Perfil perfilSalvo = cadastrarPerfilUseCase.cadastrarPerfil(perfil);

        PerfilResponse response = mapper.toResponse(perfilSalvo);

        URI endereco = uriBuilder.path("/v1/perfis/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }
}
