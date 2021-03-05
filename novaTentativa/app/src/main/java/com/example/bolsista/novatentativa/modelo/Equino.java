package com.example.bolsista.novatentativa.modelo;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.Date;

public class Equino implements Serializable {
    private String id;
    private String nome;
    private String raca;
    private Date dataNascimento;
    private String observacoes;
    private String sexo;
    private String atividade;
    private String sistemaCriacao;
    private String atividadeSemanal;
    private String itensidadeAtividade;
    private boolean suplementacaoMineral;
    private String temperamento;
    private boolean vicio;
    private boolean isProblemaSaude;
    private String problemaSaude;

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

    public Equino(String nome, String raca, Date dataNascimento, String observacoes,
                  String sexo, String atividade, String sistemaCriacao, String atividadeSemanal,
                  String itensidadeAtividade, boolean suplementacaoMineral, String temperamento,
                  boolean vicio, boolean isProblemaSaude, String problemaSaude) {
        this.id = id;
        this.nome = nome;
        this.raca = raca;
        this.dataNascimento = dataNascimento;
        this.observacoes = observacoes;
        this.sexo = sexo;
        this.atividade = atividade;
        this.sistemaCriacao = sistemaCriacao;
        this.atividadeSemanal = atividadeSemanal;
        this.itensidadeAtividade = itensidadeAtividade;
        this.suplementacaoMineral = suplementacaoMineral;
        this.temperamento = temperamento;
        this.vicio = vicio;
        this.isProblemaSaude = isProblemaSaude;
        this.problemaSaude = problemaSaude;
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

    public String getSistemaCriacao() {
        return sistemaCriacao;
    }

    public void setSistemaCriacao(String sistemaCriacao) {
        this.sistemaCriacao = sistemaCriacao;
    }

    public String getAtividadeSemanal() {
        return atividadeSemanal;
    }

    public void setAtividadeSemanal(String atividadeSemanal) {
        this.atividadeSemanal = atividadeSemanal;
    }

    public String getItensidadeAtividade() {
        return itensidadeAtividade;
    }

    public void setItensidadeAtividade(String itensidadeAtividade) {
        this.itensidadeAtividade = itensidadeAtividade;
    }

    public boolean isSuplementacaoMineral() {
        return suplementacaoMineral;
    }

    public void setSuplementacaoMineral(boolean suplementacaoMineral) {
        this.suplementacaoMineral = suplementacaoMineral;
    }

    public String getTemperamento() {
        return temperamento;
    }

    public void setTemperamento(String temperamento) {
        this.temperamento = temperamento;
    }

    public boolean isVicio() {
        return vicio;
    }

    public void setVicio(boolean vicio) {
        this.vicio = vicio;
    }

    public boolean getIsProblemaSaude() {
        return isProblemaSaude;
    }

    public void setIsProblemaSaude(boolean problemaSaude) {
        isProblemaSaude = problemaSaude;
    }

    public String getProblemaSaude() {
        return problemaSaude;
    }

    public void setProblemaSaude(String problemaSaude) {
        this.problemaSaude = problemaSaude;
    }
}
