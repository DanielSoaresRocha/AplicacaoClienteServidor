package com.example.bolsista.novatentativa.modelo;

import java.util.ArrayList;
import java.util.Date;

public class Experimento2 {
    private String id;
    private String Equino;
    private String nome;
    private Date dataInicio;
    private Date dataFim;
    private ArrayList<Configuracao> testes;

    public Experimento2() {
    }

    public Experimento2(String id, String equino, String nome, Date dataInicio, Date dataFim, ArrayList<Configuracao> testes) {
        this.id = id;
        Equino = equino;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.testes = testes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquino() {
        return Equino;
    }

    public void setEquino(String equino) {
        Equino = equino;
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

    public ArrayList<Configuracao> getTestes() {
        return testes;
    }

    public void setTestes(ArrayList<Configuracao> testes) {
        this.testes = testes;
    }
}
