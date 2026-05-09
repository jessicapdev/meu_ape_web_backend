package com.br.meu_ape.dto;

import com.br.meu_ape.model.Apartamento;
import com.br.meu_ape.model.Imagens;
import com.br.meu_ape.model.Timeline;

import java.util.List;

public class EmpreendimentoDTO {

    private String titulo;
    private String status;
    private String cidade;
    private String bairro;
    private String construtora;
    private Double areaMin;
    private Double areaMax;
    private List<Integer> banheiros;
    private List<Integer> quartos;
    private List<Integer> vagas;
    private Double precoMin;
    private Double precoMax;
    private Imagens imagens;
    private String endereco;
    private List<String> tiposImoveis;
    private List<Apartamento> apartamentos;
    private List<String> diferenciais;
    private String descricao;
    private List<Timeline> timeline;
    private int views;
    private int dias;

    public EmpreendimentoDTO() {}

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getConstrutora() { return construtora; }
    public void setConstrutora(String construtora) { this.construtora = construtora; }

    public Double getAreaMin() { return areaMin; }
    public void setAreaMin(Double areaMin) { this.areaMin = areaMin; }

    public Double getAreaMax() { return areaMax; }
    public void setAreaMax(Double areaMax) { this.areaMax = areaMax; }

    public List<Integer> getBanheiros() { return banheiros; }
    public void setBanheiros(List<Integer> banheiros) { this.banheiros = banheiros; }

    public List<Integer> getQuartos() { return quartos; }
    public void setQuartos(List<Integer> quartos) { this.quartos = quartos; }

    public List<Integer> getVagas() { return vagas; }
    public void setVagas(List<Integer> vagas) { this.vagas = vagas; }

    public Double getPrecoMin() { return precoMin; }
    public void setPrecoMin(Double precoMin) { this.precoMin = precoMin; }

    public Double getPrecoMax() { return precoMax; }
    public void setPrecoMax(Double precoMax) { this.precoMax = precoMax; }

    public Imagens getImagens() { return imagens; }
    public void setImagens(Imagens imagens) { this.imagens = imagens; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public List<String> getTiposImoveis() { return tiposImoveis; }
    public void setTiposImoveis(List<String> tiposImoveis) { this.tiposImoveis = tiposImoveis; }

    public List<Apartamento> getApartamentos() { return apartamentos; }
    public void setApartamentos(List<Apartamento> apartamentos) { this.apartamentos = apartamentos; }

    public List<String> getDiferenciais() { return diferenciais; }
    public void setDiferenciais(List<String> diferenciais) { this.diferenciais = diferenciais; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<Timeline> getTimeline() { return timeline; }
    public void setTimeline(List<Timeline> timeline) { this.timeline = timeline; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public int getDias() { return dias; }
    public void setDias(int dias) { this.dias = dias; }
}