package com.example.bolsista.novatentativa.configuracao;


import java.io.Serializable;

public class Configuracao implements Serializable{
   // private static final long serialVersionUID = 1L;
    //private String mensagem;

    private int imagens[];
    private int qtdQuestoes;
    private int intervalo1;
    private int intervalo2;
    private int somErro;
    private int somAcerto;


    public Configuracao(int imagens[]){
            this.imagens = imagens;
    }

    public Configuracao(){

    }

    public int[] getImagens() {
        return imagens;
    }

    public void setImagens(int imagens[]) {
        this.imagens = imagens;
    }

    public int getQtdQuestoes() {
        return qtdQuestoes;
    }

    public void setQtdQuestoes(int qtdQuestoes) {
        this.qtdQuestoes = qtdQuestoes;
    }

    public int getIntervalo1() {
        return intervalo1;
    }

    public void setIntervalo1(int intervalo1) {
        this.intervalo1 = intervalo1;
    }

    public int getIntervalo2() {
        return intervalo2;
    }

    public void setIntervalo2(int intervalo2) {
        this.intervalo2 = intervalo2;
    }

    public int getSomErro() {
        return somErro;
    }

    public void setSomErro(int somErro) {
        this.somErro = somErro;
    }

    public int getSomAcerto() {
        return somAcerto;
    }

    public void setSomAcerto(int somAcerto) {
        this.somAcerto = somAcerto;
    }
    /*
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }*/
}
