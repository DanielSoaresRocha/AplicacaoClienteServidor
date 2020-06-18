package com.example.bolsista.novatentativa.modelo;


import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.ArrayList;

public class Configuracao implements Serializable{
    private String id;
    private String nome;
    private String detalhes;
    private ArrayList<Integer> imagens;
    private int qtdQuestoes;
    private int intervalo1;
    private int intervalo2;
    private int somErro;
    private int somAcerto;
    private boolean completo;
    private DocumentReference usuario;

    public Configuracao(){
    }

    public Configuracao(String id, String nome, String detalhes, ArrayList<Integer> imagens,
                        int qtdQuestoes, int intervalo1, int intervalo2, int somErro, int somAcerto,
                        DocumentReference usuario) {
        this.id = id;
        this.nome = nome;
        this.detalhes = detalhes;
        this.imagens = imagens;
        this.qtdQuestoes = qtdQuestoes;
        this.intervalo1 = intervalo1;
        this.intervalo2 = intervalo2;
        this.somErro = somErro;
        this.somAcerto = somAcerto;
        this.usuario = usuario;
    }

    public Configuracao(String id, String nome, String detalhes, ArrayList<Integer> imagens,
                        int qtdQuestoes, int intervalo1, int intervalo2, int somErro, int somAcerto,
                        DocumentReference usuario, Boolean completo) {
        this.id = id;
        this.nome = nome;
        this.detalhes = detalhes;
        this.imagens = imagens;
        this.qtdQuestoes = qtdQuestoes;
        this.intervalo1 = intervalo1;
        this.intervalo2 = intervalo2;
        this.somErro = somErro;
        this.somAcerto = somAcerto;
        this.usuario = usuario;
        this.completo = completo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public DocumentReference getUsuario() {
        return usuario;
    }

    public void setUsuario(DocumentReference usuario) {
        this.usuario = usuario;
    }

    public ArrayList<Integer> getImagens() {
        return imagens;
    }

    public void setImagens(ArrayList<Integer> imagens) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCompleto() {
        return completo;
    }

    public void setCompleto(boolean completo) {
        this.completo = completo;
    }

    /*
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }*/
}
