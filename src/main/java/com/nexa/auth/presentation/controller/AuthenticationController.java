package com.nexa.auth.presentation.controller;

import com.nexa.auth.application.usecase.auth.CadastrarUsuarioUseCase;
import com.nexa.auth.application.usecase.auth.RealizarLoginUseCase;
import com.nexa.auth.application.dto.auth.LoginRequestDto;
import com.nexa.auth.application.dto.auth.TokenResponseDto;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Autenticação", description = "Endpoints responsáveis pelo cadastro e autenticação de usuários.")
public class AuthenticationController {

    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final RealizarLoginUseCase realizarLoginUseCase;
    private final UsuarioControllerMapper usuarioMapper;

    public AuthenticationController(CadastrarUsuarioUseCase cadastrarUsuarioUseCase, RealizarLoginUseCase realizarLoginUseCase, UsuarioControllerMapper usuarioMapper) {
        this.cadastrarUsuarioUseCase = cadastrarUsuarioUseCase;
        this.realizarLoginUseCase = realizarLoginUseCase;
        this.usuarioMapper = usuarioMapper;
    }

    @Operation(summary = "Cadastrar usuário", description = "Realiza o cadastro de um novo usuário no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso", content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação / Email já cadastrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> cadastrarUsuario(@RequestBody @Valid UsuarioRequest request,
                                                            UriComponentsBuilder uriBuilder) {
        Usuario usuario = usuarioMapper.toDomain(request);

        Usuario usuarioSalvo = cadastrarUsuarioUseCase.cadastrarUsuario(usuario);

        UsuarioResponse response = usuarioMapper.toResponse(usuarioSalvo);

        URI endereco = uriBuilder.path("/v1/usuarios/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(summary = "Realizar login", description = "Autentica um usuário e retorna um token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content(schema = @Schema(implementation = TokenResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody @Valid LoginRequestDto dto) {
        return realizarLoginUseCase.fazerLogin(dto);
    }
}
