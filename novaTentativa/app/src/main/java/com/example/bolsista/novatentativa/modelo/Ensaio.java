package com.example.bolsista.novatentativa.modelo;

import java.io.Serializable;

public class Ensaio implements Serializable {
    private String id;
    private String idSessao;
    private String idDesafio;
    private Boolean acerto;
    private Desafio desafio;
    private long tempoAcerto;

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

    public Boolean getAcerto() {
        return acerto;
    }

    public void setAcerto(Boolean acerto) {
        this.acerto = acerto;
    }

    public Desafio getDesafio() {
        return desafio;
    }

    public void setDesafio(Desafio desafio) {
        this.desafio = desafio;
    }

    public long getTempoAcerto() {
        return tempoAcerto;
    }

    public void setTempoAcerto(long tempoAcerto) {
        this.tempoAcerto = tempoAcerto;
    }
}
