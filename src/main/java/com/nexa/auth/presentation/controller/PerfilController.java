package com.nexa.auth.presentation.controller;

import com.nexa.auth.application.usecase.perfil.CadastrarPerfilUseCase;
import com.nexa.auth.application.usecase.perfil.ListarTodosPerfisUseCase;
import com.nexa.auth.domain.entity.perfil.Perfil;
import com.nexa.auth.presentation.mapper.PerfilControllerMapper;
import com.nexa.auth.presentation.request.perfil.PerfilRequest;
import com.nexa.auth.presentation.response.perfil.PerfilResponse;
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
import com.nexa.auth.presentation.exception.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/perfis")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Perfis", description = "Gerenciamento de perfis e permissões dos usuários.")
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

    @Operation(summary = "Cadastrar perfil",
                description = """
                Cria um novo perfil no sistema.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem executar esta operação.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Perfil criado com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = PerfilResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário sem permissão",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PerfilResponse> cadastrarPerfil(@RequestBody @Valid PerfilRequest request,
                                                           UriComponentsBuilder uriBuilder) {
        Perfil perfil = mapper.toDomain(request);

        Perfil perfilSalvo = cadastrarPerfilUseCase.cadastrarPerfil(perfil);

        PerfilResponse response = mapper.toResponse(perfilSalvo);

        URI endereco = uriBuilder.path("/v1/perfis/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(
            summary = "Listar perfis",
            description = """
                Retorna uma lista paginada de perfis.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem executar esta operação.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfis retornados com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )

            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário sem permissão",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<PerfilResponse>> listarTodosPerfis(@PageableDefault(size = 10) Pageable pageable) {
        Page<PerfilResponse> perfis = listarTodosPerfisUseCase.listarTodosPerfis(pageable)
                .map(mapper::toResponse);

        return ResponseEntity.ok(perfis);
    }

}
