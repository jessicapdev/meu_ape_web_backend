package com.br.meu_ape.controller;

import com.br.meu_ape.config.JwtTokenProvider;
import com.br.meu_ape.model.Usuario;
import com.br.meu_ape.model.response.UsuarioResponse;
import com.br.meu_ape.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.Email;

@RestController
@RequestMapping(value="/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    public UsuarioController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<Void> insert(@RequestBody Usuario obj){
        service.insert(obj);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value="/perfil", method=RequestMethod.GET)
    public ResponseEntity<UsuarioResponse> find(
            @RequestHeader(value = "email", required = true)
            @Email(message = "Email inválido")
            String email,
            @RequestHeader(value = "Authorization", required = false)
            String authorization) {

        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Email é obrigatório");
        }

        if (authorization == null || authorization.trim().isEmpty()) {
            throw new AccessDeniedException("Token de autorização é obrigatório");
        }

        if (!jwtTokenProvider.validateToken(authorization)) {
            throw new AccessDeniedException("Token de autorização inválido ou expirado");
        }

        UsuarioResponse usuario = service.findUserResponse(email);
        return ResponseEntity.ok(usuario);
    }

    @RequestMapping(value="criar-conta", method=RequestMethod.PUT)
    public ResponseEntity<Void> update(@Validated  @RequestBody Usuario obj,
                                       @RequestHeader(value = "Authorization", required = false) String authorization){
        if (!StringUtils.hasText(obj.getEmail())) {
            throw new IllegalArgumentException("Email é obrigatório");
        }

        if (authorization == null || authorization.trim().isEmpty()) {
            throw new AccessDeniedException("Token de autorização é obrigatório");
        }

        Usuario existingUser = service.findUser(obj.getEmail());
        if (existingUser == null){
            throw new IllegalArgumentException("Email não encontrado na base de dados.");
        } else {
            service.update(existingUser, obj);
            return ResponseEntity.noContent().build();
        }
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value="/page", method=RequestMethod.GET)
    public ResponseEntity<Page<Usuario>> findPage(
            @RequestParam (value="page", defaultValue = "0") Integer page,
            @RequestParam (value="linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam (value="orderBy", defaultValue = "nome") String orderBy,
            @RequestParam (value="direction", defaultValue = "ASC") String direction) {
        Page<Usuario> list = service.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok().body(list);
    }

}

