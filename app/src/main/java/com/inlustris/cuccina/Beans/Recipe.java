package com.inlustris.cuccina.Beans;

import java.util.ArrayList;

public class Recipe {
    String prato,tempo,id,tipo,imageurl;
    String calorias;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    ArrayList<Ingredient> ingredientes;
    ArrayList<Step> passos;

    public Recipe() {
    }


    public Recipe(String nome, String tipo, String imageurl, String calorias, String tempo) {
        this.prato = nome;
        this.tipo = tipo;
        this.imageurl = imageurl;
        this.calorias = calorias;
        this.tempo = tempo;

    }

    public String getPrato() {
        return prato;
    }

    public void setPrato(String prato) {
        this.prato = prato;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getCalorias() {
        return calorias;
    }

    public void setCalorias(String calorias) {
        this.calorias = calorias;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }
}
