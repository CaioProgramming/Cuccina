package com.inlustris.cuccina.Beans;

public class Step {
    String count;
    String passo;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Step(String count, String passo) {

        this.count = count;
        this.passo = passo;
    }

    public Step() {
    }

    public String getPasso() {
        return passo;
    }

    public void setPasso(String passo) {
        this.passo = passo;
    }
}
