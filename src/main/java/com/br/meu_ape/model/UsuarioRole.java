package com.br.meu_ape.model;

public enum UsuarioRole {
  ROLE_ADMIN, ROLE_CLIENT;

  public String getAuthority() {
    return name();
  }

}
