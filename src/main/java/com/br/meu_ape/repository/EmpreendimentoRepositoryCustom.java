package com.br.meu_ape.repository;

import com.br.meu_ape.dto.EmpreendimentoFiltroDTO;
import com.br.meu_ape.model.Empreendimento;
import com.br.meu_ape.model.projection.EmpreendimentoHomeProjection;
import org.springframework.data.domain.Page;

public interface EmpreendimentoRepositoryCustom {

    Page<EmpreendimentoHomeProjection> buscarComFiltros(EmpreendimentoFiltroDTO filtro);
}