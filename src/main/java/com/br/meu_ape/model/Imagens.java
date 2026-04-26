package com.br.meu_ape.model;

import java.util.List;

public class Imagens {
    private String banner;
    private String map;
    private List<String> plantas;
    private List<String> galeria;

    public Imagens() {}

    public String getBanner() { return banner; }
    public void setBanner(String banner) { this.banner = banner; }

    public String getMap() { return map; }
    public void setMap(String map) { this.map = map; }

    public List<String> getPlantas() { return plantas; }
    public void setPlantas(List<String> plantas) { this.plantas = plantas; }

    public List<String> getGaleria() { return galeria; }
    public void setGaleria(List<String> galeria) { this.galeria = galeria; }
}