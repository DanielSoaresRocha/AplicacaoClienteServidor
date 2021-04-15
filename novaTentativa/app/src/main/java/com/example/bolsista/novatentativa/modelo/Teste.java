package com.example.bolsista.novatentativa.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Teste implements Serializable{
    private String id;
    private String nome;
    private int intervalo1;
    private int intervalo2;
    private int qtdDesafios;
    private int somAcerto;
    private int somErro;
    private ArrayList<Desafio> desafios;
    private String observacoes;
    private int tipo;
    private int aleatoriedade;
    private ArrayList<Sessao> sessoes;
    private Boolean preTeste;
    private int qtdQuestoesPorSessao;
    private int maxVezesConsecutivas;
    private int criterioAprendizagem;
    private boolean completo;

    public Teste(){
    }

    public Teste(String id, String nome, int intervalo1, int intervalo2, int qtdDesafios,
                 int somAcerto, int somErro, ArrayList<Desafio> desafios, String observacoes,
                 int tipo, int aleatoriedade, ArrayList<Sessao> sessoes, Boolean preTeste,
                 int qtdEnsaiosPorSessao, int maxVezesConsecutivas, int criterioAprendizagem,
                 boolean completo) {
        this.id = id;
        this.nome = nome;
        this.intervalo1 = intervalo1;
        this.intervalo2 = intervalo2;
        this.qtdDesafios = qtdDesafios;
        this.somAcerto = somAcerto;
        this.somErro = somErro;
        this.desafios = desafios;
        this.observacoes = observacoes;
        this.tipo = tipo;
        this.aleatoriedade = aleatoriedade;
        this.sessoes = sessoes;
        this.preTeste = preTeste;
        this.qtdQuestoesPorSessao = qtdEnsaiosPorSessao;
        this.maxVezesConsecutivas = maxVezesConsecutivas;
        this.criterioAprendizagem = criterioAprendizagem;
        this.completo = completo;
    }

    // verificar se as duas ultimas sessões obtveram sucesso
    public void verificaAprendizagem(){
        int qtdSessoes = sessoes.size();

        if(qtdSessoes > 1){
            if(sessoes.get(qtdSessoes-1).getTaxaAcerto() >= criterioAprendizagem &&
            sessoes.get(qtdSessoes-2).getTaxaAcerto() >= criterioAprendizagem){
                setCompleto(true);
            }
        }
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

    public int getQtdDesafios() {
        return qtdDesafios;
    }

    public void setQtdDesafios(int qtdDesafios) {
        this.qtdDesafios = qtdDesafios;
    }

    public int getSomAcerto() {
        return somAcerto;
    }

    public void setSomAcerto(int somAcerto) {
        this.somAcerto = somAcerto;
    }

    public int getSomErro() {
        return somErro;
    }

    public void setSomErro(int somErro) {
        this.somErro = somErro;
    }

    public ArrayList<Desafio> getDesafios() {
        return desafios;
    }

    public void setDesafios(ArrayList<Desafio> desafios) {
        this.desafios = desafios;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getAleatoriedade() {
        return aleatoriedade;
    }

    public void setAleatoriedade(int aleatoriedade) {
        this.aleatoriedade = aleatoriedade;
    }

    public ArrayList<Sessao> getSessoes() {
        return sessoes;
    }

    public void setSessoes(ArrayList<Sessao> sessoes) {
        this.sessoes = sessoes;
    }

    public Boolean getPreTeste() {
        return preTeste;
    }

    public void setPreTeste(Boolean preTeste) {
        this.preTeste = preTeste;
    }

    public int getQtdQuestoesPorSessao() {
        return qtdQuestoesPorSessao;
    }

    public void setQtdQuestoesPorSessao(int qtdQuestoesPorSessao) {
        this.qtdQuestoesPorSessao = qtdQuestoesPorSessao;
    }

    public int getMaxVezesConsecutivas() {
        return maxVezesConsecutivas;
    }

    public void setMaxVezesConsecutivas(int maxVezesConsecutivas) {
        this.maxVezesConsecutivas = maxVezesConsecutivas;
    }

    public int getCriterioAprendizagem() {
        return criterioAprendizagem;
    }

    public void setCriterioAprendizagem(int criterioAprendizagem) {
        this.criterioAprendizagem = criterioAprendizagem;
    }

    public boolean isCompleto() {
        return completo;
    }

    public void setCompleto(boolean completo) {
        this.completo = completo;
    }
}
