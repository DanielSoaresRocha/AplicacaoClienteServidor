package com.example.bolsista.novatentativa.modelo;


import java.util.ArrayList;
import java.util.Date;

public class Experimento {
    private String id;
    private Equino equino;
    private String nome;
    private Date dataInicio;
    private Date dataFim;
    private ArrayList<Teste> testes;
    private Boolean finalizado;

    public Experimento() {
    }

    public Experimento(String id, Equino equino, String nome, Date dataInicio, Date dataFim,
                       ArrayList<Teste> testes, Boolean finalizado) {
        this.id = id;
        this.equino = equino;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.testes = testes;
        this.finalizado = finalizado;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Equino getEquino() {
        return equino;
    }

    public void setEquino(Equino equino) {
        this.equino = equino;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public ArrayList<Teste> getTestes() {
        return testes;
    }

    public void setTestes(ArrayList<Teste> testes) {
        this.testes = testes;
    }
}
