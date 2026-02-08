package com.br.meu_ape.model.request;


public class UsuarioRequest {
    private String email;
    private String senha;

    public UsuarioRequest() {};

    public UsuarioRequest(String nome, String senha) {
        this.email = nome;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String nome) {
        this.email = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
