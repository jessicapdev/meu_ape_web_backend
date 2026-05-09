package com.br.meu_ape.model.projection;

import com.br.meu_ape.model.Timeline;

import java.util.List;

public interface EmpreendimentoEmpreendimentoProjection {
    String getId();
    String getTitulo();
    String getDescricao();
    List<String> getTiposImoveis();
    String getConstrutora();
    String getEndereco();
    String getCidade();
    String getBairro();
    String getStatus();
    Double getAreaMin();
    Double getAreaMax();
    List<Integer> getBanheiros();
    List<Integer> getQuartos();
    List<Integer> getVagas();
    List<Timeline> getTimeline();
    double getPrecoMin();
    double getPrecoMax();
    List<String> getDiferenciais();
}
