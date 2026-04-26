package com.br.meu_ape.model;

public class ItemDescricao {
    private String nome;

    public ItemDescricao() {}

    public ItemDescricao(String nome) {
        this.nome = nome;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}