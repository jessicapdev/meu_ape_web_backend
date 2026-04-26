package com.br.meu_ape.repository;

import com.br.meu_ape.model.Empreendimento;
import com.br.meu_ape.model.projection.EmpreendimentoEmpreendimentoProjection;
import com.br.meu_ape.model.projection.EmpreendimentoHomeProjection;
import com.br.meu_ape.model.projection.EmpreendimentoImagemProjection;
import com.br.meu_ape.model.projection.EmpreendimentoPerfilProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpreendimentoRepository extends MongoRepository<Empreendimento, String>, EmpreendimentoRepositoryCustom {

    Page<EmpreendimentoHomeProjection> findHomeBy(Pageable pageable);
    Page<EmpreendimentoPerfilProjection> findPerfilBy(Pageable pageable);
    Optional<EmpreendimentoImagemProjection> findProjectedById(String id);
    Optional<EmpreendimentoEmpreendimentoProjection> findEmpreendimentoProjectedById(String id);

    @Query(value = "{ '_id': ?0 }", fields = "{ 'apartamentos': 1 }")
    Optional<Empreendimento> findApartamentosByEmpreendimentoId(String id);
    List<Empreendimento> findByCidadeIgnoreCase(String cidade);
    List<Empreendimento> findByStatusIgnoreCase(String status);


}