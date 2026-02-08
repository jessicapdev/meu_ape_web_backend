package com.br.meu_ape.model.response;

import com.br.meu_ape.model.Usuario;
import com.br.meu_ape.model.UsuarioRole;

import java.util.List;

public class UsuarioResponse {
    private String nome;
    private String email;
    private String foto;
    private String token;
    private List<UsuarioRole> usuarioRole;

    public UsuarioResponse(String nome, String email, String foto, String token, List<UsuarioRole> usuarioRole) {
        this.nome = nome;
        this.email = email;
        this.foto = foto;
        this.token = token;
        this.usuarioRole = usuarioRole;
    }

    public UsuarioResponse() {
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsuarioRole(List<UsuarioRole> usuarioRole) {
        this.usuarioRole = usuarioRole;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getFoto() {
        return foto;
    }

    public String getToken() {
        return token;
    }

    public List<UsuarioRole> getUsuarioRole() {
        return usuarioRole;
    }

    public void fromSignin(String token, Usuario usuario){
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.foto = usuario.getFoto();
        this.usuarioRole = usuario.getUsuarioRoles();
        this.token = token;
    }

    public void parseUsuario(Usuario usuario){
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.foto = usuario.getFoto();
        this.usuarioRole = usuario.getUsuarioRoles();
    }
}
