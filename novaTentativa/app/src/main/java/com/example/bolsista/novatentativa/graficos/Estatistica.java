package com.example.bolsista.novatentativa.graficos;

import com.example.bolsista.novatentativa.modelo.Sessao;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Estatistica {
    Double valY[] = new Double[100];
    DecimalFormat f = new DecimalFormat("0.00");
    ArrayList<Sessao> sessoes;

    public Estatistica(ArrayList<Sessao> sessoes){
        this.sessoes = sessoes;
    }

    public String getMedia(){
        return f.format(calculaMedia(sessoes));
    }

    public String getMediana(){
        return f.format(calculaMediana(sessoes));
    }

    public String getDesvioPadrao(){
        return f.format(calculaDesvioPadrao(sessoes));
    }

    private double calculaMedia(ArrayList<Sessao> sessoes){
        double media = 0;
        for(int i = 0; i < sessoes.size(); i++){
            media = media+sessoes.get(i).getTaxaAcerto();
        }
        media = media/sessoes.size();

        return media;
    }

    private double calculaMediana(ArrayList<Sessao> sessoes){
        double mediana = 0;

        for (int i = 0; i < sessoes.size(); i++) {
            valY[i] = sessoes.get(i).getTaxaAcerto();
        }
        ordena(sessoes);
        for (int i = 0; i < sessoes.size(); i++) {
            System.out.println("Porcentagem: "+valY[i]);
        }

        if(sessoes.size() % 2 == 0){
            int posicao = ((sessoes.size()/2) - 1);
            mediana = (valY[posicao]+valY[posicao+1])/2;
            System.out.println("Par: ValY["+posicao+"] = "+valY[posicao]);
        }else {
            int posicao = ((sessoes.size() - 1) / 2);
            System.out.println("Impar: ValY["+posicao+"] = "+valY[posicao]);
            mediana = valY[posicao];
        }

        return mediana;
    }

    private void ordena(ArrayList<Sessao> sessoes){
        for (int i = 0; i < sessoes.size(); i++)
        {
            for (int j = 0; j < sessoes.size(); j++)
            {
                if (valY[i] < valY[j])
                {
                    //aqui acontece a troca. O maior vai para a direita e o menor para a esquerda
                    double auxY = valY[i];

                    valY[i] = valY[j];

                    valY[j] = auxY;
                }
            }
        }
    }//Termina método

    private double calculaDesvioPadrao(ArrayList<Sessao> sessoes){
        double media = calculaMedia(sessoes);
        double variancia = 0;

        for(Sessao sessao : sessoes){
            variancia += Math.pow(sessao.getTaxaAcerto() - media, 2);
        }

        variancia = variancia / sessoes.size();

        return Math.sqrt(variancia); // Desvio padrão
    }
}