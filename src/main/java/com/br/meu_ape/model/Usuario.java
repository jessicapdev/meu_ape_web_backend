package com.br.meu_ape.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuario")
public class Usuario {

    @Id
    private String id;
    private String nome;
    private String email;
    private String foto;
    private String senha;
    private String telefone;

    List<UsuarioRole> usuarioRoles;

    public Usuario() {
    }

    public Usuario(String id, String nome, String email, String foto, String senha, String telefone, List<UsuarioRole> usuarioRoles) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.foto = foto;
        this.senha = senha;
        this.telefone = telefone;
        this.usuarioRoles = usuarioRoles;
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() { return telefone; }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<UsuarioRole> getUsuarioRoles() {
        return usuarioRoles;
    }

    public void setUsuarioRoles(List<UsuarioRole> usuarioRoles) {
        this.usuarioRoles = usuarioRoles;
    }

}