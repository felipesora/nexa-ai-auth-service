package com.nexa.auth.presentation.controller;

import com.nexa.auth.application.usecase.usuario.CadastrarUsuarioUseCase;
import com.nexa.auth.application.usecase.usuario.ListarTodosUsuariosUseCase;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.presentation.mapper.UsuarioControllerMapper;
import com.nexa.auth.presentation.request.usuario.UsuarioRequest;
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
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final ListarTodosUsuariosUseCase listarTodosUsuariosUseCase;
    private final UsuarioControllerMapper usuarioMapper;

    public UsuarioController(CadastrarUsuarioUseCase cadastrarUsuarioUseCase,
                             ListarTodosUsuariosUseCase listarTodosUsuariosUseCase,
                             UsuarioControllerMapper usuarioMapper) {
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.listarTodosUsuariosUseCase = listarTodosUsuariosUseCase;
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

    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listarTodosUsuarios(@PageableDefault(size = 10)Pageable pageable) {
        Page<UsuarioResponse> usuarios = listarTodosUsuariosUseCase.listarTodosUsuarios(pageable)
                .map(usuarioMapper::toResponse);

        return ResponseEntity.ok(usuarios);
    }
}
