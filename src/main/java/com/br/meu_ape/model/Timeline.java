package com.br.meu_ape.model;

public record Timeline(
        Integer ordem,
        String titulo,
        String data,
        boolean completado
) {}
