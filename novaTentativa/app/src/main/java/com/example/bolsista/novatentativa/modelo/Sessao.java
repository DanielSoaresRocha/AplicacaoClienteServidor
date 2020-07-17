package com.example.bolsista.novatentativa.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Sessao implements Serializable {
    private String id;
    private Teste teste;
    private Usuario experimentador;
    private String nome;
    private ArrayList<Ensaio> ensaios;
    private Date data;
    private int taxaAcerto;

    public Sessao() {
    }

    public Sessao(String id, String nome, Date data, Usuario experimentador) {
        this.id = id;
        this.nome = nome;
        this.data = data;
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

    public Teste getTeste() {
        return teste;
    }

    public void setTeste(Teste teste) {
        this.teste = teste;
    }

    public ArrayList<Ensaio> getEnsaios() {
        return ensaios;
    }

    public void setEnsaios(ArrayList<Ensaio> ensaios) {
        this.ensaios = ensaios;
    }
}
