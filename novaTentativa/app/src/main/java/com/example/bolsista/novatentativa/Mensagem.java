package com.example.bolsista.novatentativa;


import java.io.Serializable;

public class Mensagem implements Serializable{
    //private static final long serialVersionUID = 1L;
    private int imagens[];
    private String mensagem;


    public Mensagem(int imagens[]){
            this.imagens = imagens;
    }

    public int[] getImagens() {
        return imagens;
    }

    public void setImagens(int imagens[]) {
        this.imagens = imagens;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
