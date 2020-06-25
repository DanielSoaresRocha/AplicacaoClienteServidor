package com.example.bolsista.novatentativa.modelo;

public class Ensaio {
    private int id;
    private int idSessao;
    private int idDesafio;
    private int tempoAcerto;
    private Boolean acerto;

    public Ensaio(int id, int idSessao, int idDesafio, int tempoAcerto, Boolean acerto) {
        this.id = id;
        this.idSessao = idSessao;
        this.idDesafio = idDesafio;
        this.tempoAcerto = tempoAcerto;
        this.acerto = acerto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSessao() {
        return idSessao;
    }

    public void setIdSessao(int idSessao) {
        this.idSessao = idSessao;
    }

    public int getIdDesafio() {
        return idDesafio;
    }

    public void setIdDesafio(int idDesafio) {
        this.idDesafio = idDesafio;
    }

    public int getTempoAcerto() {
        return tempoAcerto;
    }

    public void setTempoAcerto(int tempoAcerto) {
        this.tempoAcerto = tempoAcerto;
    }

    public Boolean getAcerto() {
        return acerto;
    }

    public void setAcerto(Boolean acerto) {
        this.acerto = acerto;
    }
}
