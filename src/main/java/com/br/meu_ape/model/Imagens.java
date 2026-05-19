package com.br.meu_ape.model;

import java.util.ArrayList;
import java.util.List;

public class Imagens {

    private ImagemItem banner;
    private ImagemItem map;
    private List<ImagemItem> plantas = new ArrayList<>();
    private List<ImagemItem> galeria = new ArrayList<>();

    public Imagens() {}

    public ImagemItem getBanner() {
        return banner;
    }

    public void setBanner(ImagemItem banner) {
        this.banner = banner;
    }

    public ImagemItem getMap() {
        return map;
    }

    public void setMap(ImagemItem map) {
        this.map = map;
    }

    public List<ImagemItem> getPlantas() {
        return plantas;
    }

    public void setPlantas(List<ImagemItem> plantas) {
        this.plantas = plantas;
    }

    public List<ImagemItem> getGaleria() {
        return galeria;
    }

    public void setGaleria(List<ImagemItem> galeria) {
        this.galeria = galeria;
    }
}