package com.example.bolsista.novatentativa.modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String uid;
    private String nome;

    public Usuario() {
    }

    public Usuario(String uid, String nome) {
        this.uid = uid;
        this.nome = nome;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString()
    {
        return nome;
    }
}
