package com.example.bolsista.novatentativa.modelo;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Experimento {
    private String id;
    private DocumentReference configuracao;
    private DocumentReference usuario;
    private DocumentReference equino;
    private Date data;
    private String descricao;
    private int number;

    public Experimento() {
    }

    public Experimento(String id, DocumentReference configuracao, DocumentReference usuario,
                       DocumentReference equino, Date data, String descricao, int number) {
        this.id = id;
        this.configuracao = configuracao;
        this.usuario = usuario;
        this.equino = equino;
        this.data = data;
        this.descricao = descricao;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentReference getConfiguracao() {
        return configuracao;
    }

    public void setConfiguracao(DocumentReference configuracao) {
        this.configuracao = configuracao;
    }

    public DocumentReference getUsuario() {
        return usuario;
    }

    public void setUsuario(DocumentReference usuario) {
        this.usuario = usuario;
    }

    public DocumentReference getEquino() {
        return equino;
    }

    public void setEquino(DocumentReference equino) {
        this.equino = equino;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
