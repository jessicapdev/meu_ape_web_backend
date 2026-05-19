package com.br.meu_ape.model;

public class ImagemItem {
    private String fileId;
    private String titulo;
    private String descricao;

    public ImagemItem() {}

    public ImagemItem(String fileId, String titulo, String descricao) {
        this.fileId = fileId;
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}