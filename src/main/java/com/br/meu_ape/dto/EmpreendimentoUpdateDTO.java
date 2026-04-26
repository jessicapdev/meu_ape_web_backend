package com.br.meu_ape.dto;

import java.util.List;

public record EmpreendimentoUpdateDTO (
        String titulo,
        String descricao,
        List<String> tiposImoveis,
        String endereco,
        String construtora,
        String cidade,
        String bairro,
        String status,
        Double areaMin,
        Double areaMax,
        List<Integer> banheiros,
        List<Integer> quartos,
        List<Integer> vagas,
        double precoMin,
        double precoMax,
        List<String> diferenciais
) {}
