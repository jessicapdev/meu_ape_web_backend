package com.br.meu_ape.model.projection;


import java.util.List;

public interface EmpreendimentoPerfilProjection {
    String getId();
    String getTitulo();
    String getStatus();
    String getCidade();
    String getConstrutora();
    ImagensProjection getImagens();

    interface ImagensProjection {
        ImagemItemProjection getBanner();
        ImagemItemProjection getMap();
        List<ImagemItemProjection> getPlantas();
        List<ImagemItemProjection> getGaleria();
    }

    interface ImagemItemProjection {
        String getFileId();
        String getTitulo();
        String getDescricao();
    }
}