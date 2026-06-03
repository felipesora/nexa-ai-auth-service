package com.nexa.auth.presentation.controller;

import com.nexa.auth.application.usecase.usuario.*;
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

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    private final ListarTodosUsuariosUseCase listarTodosUsuariosUseCase;
    private final ListarUsuariosPorPerfilUseCase listarUsuariosPorPerfilUseCase;
    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final DesativarUsuarioUseCase desativarUsuario;
    private final AtivarUsuarioUseCase ativarUsuarioUseCase;
    private final UsuarioControllerMapper usuarioMapper;

    public UsuarioController(ListarTodosUsuariosUseCase listarTodosUsuariosUseCase,
                             ListarUsuariosPorPerfilUseCase listarUsuariosPorPerfilUseCase,
                             BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase,
                             AtualizarUsuarioUseCase atualizarUsuarioUseCase,
                             DesativarUsuarioUseCase desativarUsuario,
                             AtivarUsuarioUseCase ativarUsuarioUseCase,
                             UsuarioControllerMapper usuarioMapper) {
        this.listarTodosUsuariosUseCase = listarTodosUsuariosUseCase;
        this.listarUsuariosPorPerfilUseCase = listarUsuariosPorPerfilUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
        this.desativarUsuario = desativarUsuario;
        this.ativarUsuarioUseCase = ativarUsuarioUseCase;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listarTodosUsuarios(@PageableDefault(size = 10)Pageable pageable) {
        Page<UsuarioResponse> usuarios = listarTodosUsuariosUseCase.listarTodosUsuarios(pageable)
                .map(usuarioMapper::toResponse);

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/perfil")
    public ResponseEntity<Page<UsuarioResponse>> listarUsuariosPorPerfil(@RequestParam String nomePerfil, @PageableDefault(size = 10) Pageable pageable) {
        Page<UsuarioResponse> usuarios = listarUsuariosPorPerfilUseCase.listarUsuariosPorPerfil(nomePerfil, pageable)
                .map(usuarioMapper::toResponse);

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = buscarUsuarioPorIdUseCase.buscarUsuarioPorId(id);
        UsuarioResponse response = usuarioMapper.toResponse(usuario);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioRequest request) {
        Usuario usuario = usuarioMapper.toDomain(request);
        atualizarUsuarioUseCase.atualizarUsuario(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarUsuario(@PathVariable Long id) {
        desativarUsuario.desativarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarUsuario(@PathVariable Long id) {
        ativarUsuarioUseCase.ativarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
