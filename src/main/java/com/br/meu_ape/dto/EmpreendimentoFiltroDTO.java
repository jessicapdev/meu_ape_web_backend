package com.br.meu_ape.dto;

import java.util.List;

public class EmpreendimentoFiltroDTO {

    private Integer page = 0;
    private Integer size = 10;
    private List<Integer> quartos;
    private Double precoMin;
    private Double precoMax;
    private List<String> status;
    private Double areaMin;
    private Double areaMax;
    private List<Integer> banheiros;
    private List<Integer> vagas;
    private List<String> tiposImoveis;
    private List<String> diferenciais;
    private String sortBy = "titulo";
    private String sortDirection = "ASC";
    private String search;

    public EmpreendimentoFiltroDTO() {}

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

    public List<Integer> getQuartos() { return quartos; }
    public void setQuartos(List<Integer> quartos) { this.quartos = quartos; }

    public Double getPrecoMin() { return precoMin; }
    public void setPrecoMin(Double precoMin) { this.precoMin = precoMin; }

    public Double getPrecoMax() { return precoMax; }
    public void setPrecoMax(Double precoMax) { this.precoMax = precoMax; }

    public List<String> getStatus() { return status; }
    public void setStatus(List<String> status) { this.status = status; }

    public Double getAreaMin() { return areaMin; }
    public void setAreaMin(Double areaMin) { this.areaMin = areaMin; }

    public Double getAreaMax() { return areaMax; }
    public void setAreaMax(Double areaMax) { this.areaMax = areaMax; }

    public List<Integer> getBanheiros() { return banheiros; }
    public void setBanheiros(List<Integer> banheiros) { this.banheiros = banheiros; }

    public List<Integer> getVagas() { return vagas; }
    public void setVagas(List<Integer> vagas) { this.vagas = vagas; }

    public List<String> getTiposImoveis() { return tiposImoveis; }
    public void setTiposImoveis(List<String> tiposImoveis) { this.tiposImoveis = tiposImoveis; }

    public List<String> getDiferenciais() { return diferenciais; }
    public void setDiferenciais(List<String> diferenciais) { this.diferenciais = diferenciais; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }

    public String getSearch() { return search; }
    public void setSearch(String search) { this.search = search; }
}
