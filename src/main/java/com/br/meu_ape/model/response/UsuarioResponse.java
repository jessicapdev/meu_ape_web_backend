package com.br.meu_ape.model.response;

import com.br.meu_ape.model.Usuario;

import java.util.List;


public class UsuarioResponse {
    private String nome;
    private String email;
    private String foto;
    private List<String> roles;
    private String token;
    private String refreshToken;
    private String telefone;

    public UsuarioResponse(String nome, String email, String foto, String telefone) {
        this.nome = nome;
        this.email = email;
        this.foto = foto;
        this.telefone = telefone;
    }

    public UsuarioResponse(String nome, String token, String refreshToken) {
        this.nome = nome;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public UsuarioResponse() { }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<String> getRoles() { return roles; }

    public void setRoles(List<String> roles) { this.roles = roles; }

    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getFoto() { return foto; }

    public String getToken() { return token; }

    public String getRefreshToken() { return refreshToken; }

    public String getTelefone() { return telefone; }

    public void fromSignin(String token, String refresh, Usuario usuario){
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.token = token;
        this.refreshToken = refresh;
    }

    public void fromPerfil(String token, Usuario usuario){
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.foto = usuario.getFoto();
        this.telefone = usuario.getTelefone();
    }

    public void parseUsuario(Usuario usuario){
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.foto = usuario.getFoto();
        this.telefone = usuario.getTelefone();
    }
}
