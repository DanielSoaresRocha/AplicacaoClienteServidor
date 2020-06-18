package com.example.bolsista.novatentativa.modelo;

import java.util.Date;

public class Sessao {
    private String id;
    private String nome;
    private Date data;
    private int taxaAcerto;
    private Usuario experimentador;

    public Sessao(String id, String nome, Date data, int taxaAcerto, Usuario experimentador) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.taxaAcerto = taxaAcerto;
        this.experimentador = experimentador;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getTaxaAcerto() {
        return taxaAcerto;
    }

    public void setTaxaAcerto(int taxaAcerto) {
        this.taxaAcerto = taxaAcerto;
    }

    public Usuario getExperimentador() {
        return experimentador;
    }

    public void setExperimentador(Usuario experimentador) {
        this.experimentador = experimentador;
    }
}
