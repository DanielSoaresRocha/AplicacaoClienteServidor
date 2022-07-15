package com.example.bolsista.novatentativa.modelo;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Experimento implements Serializable {
    private String id;
    private String idExperimentador;
    private Equino equino;
    private String nome;
    private Date dataInicio;
    private Date dataFim;
    private ArrayList<Teste> testes;
    private Boolean finalizado;

    public Experimento() {
    }

    public Experimento(String id, String idExperimentador, Equino equino, String nome, Date dataInicio, Date dataFim,
                       ArrayList<Teste> testes, Boolean finalizado) {
        this.id = id;
        this.equino = equino;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.testes = testes;
        this.finalizado = finalizado;
        this.idExperimentador = idExperimentador;
    }

    // Se todos os testes tiverem sido completados o Experimento é finalizado,
    // se não, continua em andamento.
    public void verificarCompleto() {
        boolean finalizado = true;

        for(Teste teste: testes){
            if(!teste.isCompleto()){
                finalizado = false;
                break;
            }
        }

        setFinalizado(finalizado);
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

    public String getIdExperimentador() {
        return idExperimentador;
    }

    public void setIdExperimentador(String idExperimentador) {
        this.idExperimentador = idExperimentador;
    }
}
