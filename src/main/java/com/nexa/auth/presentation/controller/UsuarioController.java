package com.nexa.auth.presentation.controller;

import com.nexa.auth.application.usecase.usuario.CadastrarUsuarioUseCase;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.presentation.mapper.UsuarioControllerMapper;
import com.nexa.auth.presentation.request.usuario.UsuarioRequest;
import com.nexa.auth.presentation.response.usuario.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final UsuarioControllerMapper usuarioMapper;

    public UsuarioController(CadastrarUsuarioUseCase cadastrarUsuarioUseCase, UsuarioControllerMapper usuarioMapper) {
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.usuarioMapper = usuarioMapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrarUsuario(@RequestBody @Valid UsuarioRequest request,
                                                            UriComponentsBuilder uriBuilder) {
        Usuario usuario = usuarioMapper.toDomain(request);

        Usuario usuarioSalvo = cadastrarUsuarioUseCase.cadastrarUsuario(usuario);

        UsuarioResponse response = usuarioMapper.toResponse(usuarioSalvo);

        URI endereco = uriBuilder.path("/v1/usuarios/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }
}
