package com.example.bolsista.novatentativa.modelo;

import java.io.Serializable;

public class Mensagem implements Serializable {
    private int identificacao;
    private int comando;

    public Mensagem(int identificacao, int comando) {
        this.identificacao = identificacao;
        this.comando = comando;
    }

    public Mensagem() {
    }

    public int getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(int identificacao) {
        this.identificacao = identificacao;
    }

    public int getComando() {
        return comando;
    }

    public void setComando(int comando) {
        this.comando = comando;
    }
}
