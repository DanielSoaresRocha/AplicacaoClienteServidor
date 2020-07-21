package com.example.bolsista.novatentativa.modelo;

public class Desafio {
    private String id;
    private int imgCorreta;
    private int img1;
    private int img2;

    public Desafio() {
    }

    public Desafio(String id,int img1, int imgCorreta, int img2) {
        this.id = id;
        this.imgCorreta = imgCorreta;
        this.img1 = img1;
        this.img2 = img2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImgCorreta() {
        return imgCorreta;
    }

    public void setImgCorreta(int imgCorreta) {
        this.imgCorreta = imgCorreta;
    }

    public int getImg1() {
        return img1;
    }

    public void setImg1(int img1) {
        this.img1 = img1;
    }

    public int getImg2() {
        return img2;
    }

    public void setImg2(int img2) {
        this.img2 = img2;
    }
}
