package com.nexa.auth.presentation.controller;

import com.nexa.auth.application.usecase.usuario.*;
import com.nexa.auth.domain.entity.usuario.Usuario;
import com.nexa.auth.presentation.exception.ErrorResponse;
import com.nexa.auth.presentation.mapper.UsuarioControllerMapper;
import com.nexa.auth.presentation.request.usuario.UsuarioRequest;
import com.nexa.auth.presentation.response.usuario.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/usuarios")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema.")
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

    @Operation(
            summary = "Listar usuários",
            description = """
            Retorna uma lista paginada de usuários.

            Requer autenticação JWT.
            Apenas usuários com ROLE_ADMIN podem acessar.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listarTodosUsuarios(@PageableDefault(size = 10)Pageable pageable) {
        Page<UsuarioResponse> usuarios = listarTodosUsuariosUseCase.listarTodosUsuarios(pageable)
                .map(usuarioMapper::toResponse);

        return ResponseEntity.ok(usuarios);
    }

    @Operation(
            summary = "Listar usuários por perfil",
            description = """
            Retorna uma lista paginada de usuários pertencentes ao perfil informado.

            Requer autenticação JWT.
            Apenas usuários com ROLE_ADMIN podem acessar.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Perfil inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/perfil")
    public ResponseEntity<Page<UsuarioResponse>> listarUsuariosPorPerfil(@RequestParam String nomePerfil, @PageableDefault(size = 10) Pageable pageable) {
        Page<UsuarioResponse> usuarios = listarUsuariosPorPerfilUseCase.listarUsuariosPorPerfil(nomePerfil, pageable)
                .map(usuarioMapper::toResponse);

        return ResponseEntity.ok(usuarios);
    }


    @Operation(
            summary = "Buscar usuário por ID",
            description = """
            Retorna os dados de um usuário.

            O usuário pode consultar apenas os próprios dados,
            exceto administradores.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = buscarUsuarioPorIdUseCase.buscarUsuarioPorId(id);
        UsuarioResponse response = usuarioMapper.toResponse(usuario);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Atualizar usuário",
            description = """
            Atualiza os dados de um usuário.

            O usuário pode atualizar apenas os próprios dados,
            exceto administradores.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioRequest request) {
        Usuario usuario = usuarioMapper.toDomain(request);
        atualizarUsuarioUseCase.atualizarUsuario(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Desativar usuário",
            description = """
            Realiza a desativação lógica de um usuário.

            O usuário pode desativar apenas a própria conta,
            exceto administradores.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário desativado com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarUsuario(@PathVariable Long id) {
        desativarUsuario.desativarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Ativar usuário",
            description = """
            Reativa um usuário previamente desativado.

            Apenas administradores podem executar esta operação.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário ativado com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarUsuario(@PathVariable Long id) {
        ativarUsuarioUseCase.ativarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
