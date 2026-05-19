package com.br.meu_ape.model.projection;

import java.util.List;

public interface EmpreendimentoHomeProjection {
    String getId();
    String getTitulo();
    String getStatus();
    String getConstrutora();
    String getCidade();
    String getBairro();
    Double getAreaMin();
    Double getAreaMax();
    List<Integer> getBanheiros();
    List<Integer> getQuartos();
    List<Integer> getVagas();
    double getPrecoMin();

    ImagensSoloBanner getImagens();

    interface ImagensSoloBanner {
        ImagemItemProjection getBanner();
    }

    interface ImagemItemProjection {
        String getFileId();
        String getTitulo();
        String getDescricao();
    }
}