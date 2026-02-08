package com.br.meu_ape.controller;

import com.br.meu_ape.config.JwtTokenProvider;
import com.br.meu_ape.model.request.UsuarioRequest;
import com.br.meu_ape.model.response.UsuarioResponse;
import com.br.meu_ape.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ResponseEntity<UsuarioResponse> authenticateUser(
            @RequestBody UsuarioRequest user) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getSenha()
                )
        );

        var usuario = repo.findByEmail(user.getEmail());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.createToken(user.getEmail(), usuario.get().getUsuarioRoles());
        UsuarioResponse usuarioResp = new UsuarioResponse();
        usuarioResp.fromSignin(token, usuario.get());

        return ResponseEntity.ok(usuarioResp);
    }

    @RequestMapping(value="/refresh", method=RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public String refresh(HttpServletRequest req) {
        return jwtTokenProvider.createToken(req.getRemoteUser(),
                repo.findByNome(req.getRemoteUser()).getUsuarioRoles());
    }
}
