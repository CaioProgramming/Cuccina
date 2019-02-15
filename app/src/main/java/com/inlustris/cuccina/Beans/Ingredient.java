package com.inlustris.cuccina.Beans;

public class Ingredient {
    String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    String ingrediente;
    String medidas;
    String quantidade;




    public Ingredient() {
    }

    public Ingredient(String count, String ingrediente, String medidas, String quantidade) {
        this.count = count;
        this.ingrediente = ingrediente;
        this.medidas = medidas;
        this.quantidade = quantidade;
    }

    public String getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(String ingrediente) {
        this.ingrediente = ingrediente;
    }

    public String getMedidas() {
        return medidas;
    }

    public void setMedidas(String medidas) {
        this.medidas = medidas;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }
}
