package com.br.meu_ape.repository;

import com.br.meu_ape.model.Contato;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContatoRepository extends MongoRepository<Contato, String> {
    List<Contato> findAllByLido(Boolean lido);

    Optional<Contato> findById(String id);
}