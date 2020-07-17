package com.example.bolsista.novatentativa.modelo;

import java.io.Serializable;

public class Ensaio implements Serializable {
    private String id;
    private String idSessao;
    private String idDesafio;
    private int tempoAcerto;
    private Boolean acerto;

    public Ensaio(String idSessao) {
        this.idSessao = idSessao;
    }

    public Ensaio() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSessao() {
        return idSessao;
    }

    public void setIdSessao(String idSessao) {
        this.idSessao = idSessao;
    }

    public String getIdDesafio() {
        return idDesafio;
    }

    public void setIdDesafio(String idDesafio) {
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
