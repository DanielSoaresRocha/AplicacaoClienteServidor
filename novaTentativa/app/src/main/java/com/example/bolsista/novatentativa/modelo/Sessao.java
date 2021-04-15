package com.example.bolsista.novatentativa.modelo;

import android.util.Log;

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
    private Double taxaAcerto;

    public Sessao() {
    }

    public Sessao(String id, String nome, Date data, Usuario experimentador) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.experimentador = experimentador;
    }

    // Percorre todos os ensaios desta sessão e calcula a porcentagem de acerto
    public void calculaPorcentagemAcerto(){
        int qtdEnsaios = ensaios.size();
        Log.i("QTD_SESSAO", "QTD TOTAL = "+ qtdEnsaios);
        int qtdAcertos = 0;
        for (int i = 0; i < qtdEnsaios; i++){
            Log.i("QTD_SESSAO", "ensaio = "+ ensaios.get(i).getId());
            if(ensaios.get(i).getAcerto())
                qtdAcertos++;
        }
        double divisao = (double) qtdAcertos/qtdEnsaios;
        double porcentagem = (divisao*100);

        setTaxaAcerto(porcentagem);
    }

    public Double getTaxaAcerto() {
        return taxaAcerto;
    }

    public void setTaxaAcerto(Double taxaAcerto) {
        this.taxaAcerto = taxaAcerto;
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
