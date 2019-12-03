package com.example.bolsista.novatentativa.banco;

import com.google.firebase.firestore.DocumentReference;

public class Cavalo {
    private String id;
    private String nome;
    private String raca;
    private int idade;
    private String detalhes;
    private DocumentReference referencia;

    public Cavalo() {
    }

    public Cavalo(String nome, String raca, int idade, String detalhes, String id, DocumentReference referencia) {
        this.nome = nome;
        this.raca = raca;
        this.idade = idade;
        this.detalhes = detalhes;
        this.id = id;
        this.referencia = referencia;
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

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentReference getReferencia() {
        return referencia;
    }

    public void setReferencia(DocumentReference referencia) {
        this.referencia = referencia;
    }
}
