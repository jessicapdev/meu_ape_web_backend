package com.br.meu_ape.model.projection;

import com.br.meu_ape.model.Imagens;

import java.util.List;

public interface EmpreendimentoImagemProjection {
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
