package com.example.bolsista.novatentativa.modelo;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Cavalo {
    private String id;
    private String nome;
    private String raca;
    private Date dataNascimento;
    private String detalhes;
    private DocumentReference usuario; //referencia a outro documento de outra coleção ("chave estrangeira")

    public Cavalo() {
    }

    public Cavalo(String nome, String raca, Date dataNascimento, String detalhes, String id, DocumentReference usuario) {
        this.nome = nome;
        this.raca = raca;
        this.dataNascimento = dataNascimento;
        this.detalhes = detalhes;
        this.id = id;
        this.usuario = usuario;
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

    public DocumentReference getUsuario() {
        return usuario;
    }

    public void setUsuario(DocumentReference usuario) {
        this.usuario = usuario;
    }
}
