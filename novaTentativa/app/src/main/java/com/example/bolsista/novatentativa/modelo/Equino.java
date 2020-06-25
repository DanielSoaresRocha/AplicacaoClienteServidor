package com.example.bolsista.novatentativa.modelo;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Equino {
    private String id;
    private String nome;
    private String raca;
    private Date dataNascimento;
    private String observacoes;
    private String sexo;
    private String atividade;

    public Equino() {
    }

    public Equino(String nome, String raca, Date dataNascimento, String observacoes,
                  String sexo, String atividade) {
        this.nome = nome;
        this.raca = raca;
        this.dataNascimento = dataNascimento;
        this.observacoes = observacoes;
        this.sexo = sexo;
        this.atividade = atividade;
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

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }
}
