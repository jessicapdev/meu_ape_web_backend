package com.br.meu_ape.model.projection;

import com.br.meu_ape.model.Imagens;

public interface EmpreendimentoPerfilProjection {
    String getId();
    String getTitulo();
    String getStatus();
    String getCidade();
    String getConstrutora();
    Imagens getImagens();
}