package com.example.bolsista.novatentativa.modelo;

import com.google.firebase.firestore.DocumentReference;

public class Desafio {
    private String id;
    private long imgCorreta;
    private long imgErrada;
    private int qtdErros = 0;
    private long tempoAcerto;
    private DocumentReference experimento;

    public Desafio() {
    }

    public Desafio(String id, long imgCorreta, long imgErrada, int qtdErros,
                   long tempoAcerto, DocumentReference experimento) {
        this.id = id;
        this.imgCorreta = imgCorreta;
        this.imgErrada = imgErrada;
        this.qtdErros = qtdErros;
        this.tempoAcerto = tempoAcerto;
        this.experimento = experimento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getImgCorreta() {
        return imgCorreta;
    }

    public void setImgCorreta(long imgCorreta) {
        this.imgCorreta = imgCorreta;
    }

    public long getImgErrada() {
        return imgErrada;
    }

    public void setImgErrada(long imgErrada) {
        this.imgErrada = imgErrada;
    }

    public int getQtdErros() {
        return qtdErros;
    }

    public void setQtdErros(int qtdErros) {
        this.qtdErros = qtdErros;
    }

    public long getTempoAcerto() {
        return tempoAcerto;
    }

    public void setTempoAcerto(long tempoAcerto) {
        this.tempoAcerto = tempoAcerto;
    }

    public DocumentReference getExperimento() {
        return experimento;
    }

    public void setExperimento(DocumentReference experimento) {
        this.experimento = experimento;
    }
}