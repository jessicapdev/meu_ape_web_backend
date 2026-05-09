package com.br.meu_ape.repository;

import com.br.meu_ape.model.Contato;
import com.br.meu_ape.model.projection.EmpreendimentoHomeProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContatoRepository extends MongoRepository<Contato, String> {
    Page<Contato> findAllByLido(Boolean lido, Pageable pageable);
    Page<Contato> findAll(Pageable pageable);
    Optional<Contato> findById(String id);
}