package com.br.meu_ape.repository;

import com.br.meu_ape.dto.EmpreendimentoFiltroDTO;
import com.br.meu_ape.model.Empreendimento;
import com.br.meu_ape.model.projection.EmpreendimentoHomeProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.List;

@Repository
public class EmpreendimentoRepositoryImpl implements EmpreendimentoRepositoryCustom {

    private final MongoTemplate mongoTemplate;
    ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

    @Autowired
    public EmpreendimentoRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<EmpreendimentoHomeProjection> buscarComFiltros(EmpreendimentoFiltroDTO filtro) {
        Query query = new Query();

        if (filtro.getQuartos() != null && !filtro.getQuartos().isEmpty()) {
            query.addCriteria(Criteria.where("quartos").in(filtro.getQuartos()));
        }

        if (filtro.getBanheiros() != null && !filtro.getBanheiros().isEmpty()) {
            query.addCriteria(Criteria.where("banheiros").in(filtro.getBanheiros()));
        }

        if (filtro.getVagas() != null && !filtro.getVagas().isEmpty()) {
            query.addCriteria(Criteria.where("vagas").in(filtro.getVagas()));
        }

        if (filtro.getStatus() != null && !filtro.getStatus().isEmpty()) {
            query.addCriteria(Criteria.where("status").in(filtro.getStatus()));
        }

        if (filtro.getTiposImoveis() != null && !filtro.getTiposImoveis().isEmpty()) {
            query.addCriteria(Criteria.where("tiposImoveis").in(filtro.getTiposImoveis()));
        }

        if (filtro.getDiferenciais() != null && !filtro.getDiferenciais().isEmpty()) {
            query.addCriteria(Criteria.where("diferenciais").in(filtro.getDiferenciais()));
        }

        if (StringUtils.hasText(filtro.getSearch())) {
            String termoBusca = filtro.getSearch();

            Criteria searchCriteria = new Criteria().orOperator(
                    Criteria.where("titulo").regex(termoBusca, "i"),
                    Criteria.where("bairro").regex(termoBusca, "i"),
                    Criteria.where("cidade").regex(termoBusca, "i")
            );

            query.addCriteria(searchCriteria);
        }

        if (filtro.getAreaMin() != null) {
            query.addCriteria(Criteria.where("areaMax").gte(filtro.getAreaMin()));
        }

        if (filtro.getAreaMax() != null) {
            query.addCriteria(Criteria.where("areaMin").lte(filtro.getAreaMax()));
        }

        if (filtro.getPrecoMax() != null) {
            query.addCriteria(Criteria.where("precoMin").lte(filtro.getPrecoMax()));
        }

        if (filtro.getPrecoMin() != null) {
            query.addCriteria(Criteria.where("precoMax").gte(filtro.getPrecoMin()));
        }

        query.fields()
                .include("id", "titulo", "status", "construtora", "cidade", "bairro",
                        "areaMin", "areaMax", "banheiros", "quartos", "vagas",
                        "precoMin", "imagens.banner");

        int page = filtro.getPage() != null ? filtro.getPage() : 0;
        int size = filtro.getSize() != null ? filtro.getSize() : 10;
        Pageable pageable = PageRequest.of(page, size);

        long total = mongoTemplate.count(query, Empreendimento.class);
        query.with(pageable);

        List<Empreendimento> lista = mongoTemplate.find(query, Empreendimento.class);

        List<EmpreendimentoHomeProjection> projeções = lista.stream()
                .map(e -> projectionFactory.createProjection(EmpreendimentoHomeProjection.class, e))
                .toList();

        return new PageImpl<>(projeções, pageable, total);
    }

}