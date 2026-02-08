package com.br.meu_ape.repository;

import com.br.meu_ape.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Usuario findByNome(String username);

    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);

}
