package com.br.meu_ape.dto;

import java.util.List;

public record ImagemMetadadosDTO(
        String reference, // ID do GridFS (se antiga) ou Nome do Arquivo (se nova)
        String titulo,
        String descricao
) {}