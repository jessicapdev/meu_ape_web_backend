package com.br.meu_ape.repository;

import com.br.meu_ape.model.Contato;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContatoRepository extends MongoRepository<Contato, String> {
}