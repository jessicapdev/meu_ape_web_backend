package com.br.meu_ape.config;

import com.br.meu_ape.model.Usuario;
import com.br.meu_ape.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetails implements UserDetailsService {

  @Autowired
  private final UsuarioRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public MyUserDetails(UsuarioRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final Optional<Usuario> usuario = userRepository.findByEmail(username);

    if (usuario.isEmpty()) {
      throw new UsernameNotFoundException("Usuario '" + username + "' não encontrado");
    }

    return User
        .withUsername(username)
        .password(usuario.get().getSenha())
        .accountExpired(false)
        .accountLocked(false)
        .credentialsExpired(false)
        .disabled(false)
        .build();
  }

}
