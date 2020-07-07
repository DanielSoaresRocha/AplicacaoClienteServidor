package com.example.bolsista.novatentativa.viewsModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.example.bolsista.novatentativa.modelo.Teste;
import com.example.bolsista.novatentativa.modelo.Usuario;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class TesteViewModel {
    public static MutableLiveData<Teste> teste;
    public static Sessao sessao;

    public static void iniciarTeste(Teste testeDaVez, int numSessao, Usuario usuario){
        teste = new MutableLiveData<>();
        teste.setValue(testeDaVez);

        sessao = new Sessao(numSessao+"", "Sessão " + numSessao, new Date(), usuario);

        if(Objects.requireNonNull(teste.getValue()).getSessoes() == null)
            teste.getValue().setSessoes(new ArrayList<>());

        if(sessao.getEnsaios() == null) // Se não houver nenhum ensaio ainda nesta sessão
            sessao.setEnsaios(new ArrayList<>());

    }

    public static void adicionarNovaSessao(){
        sessao.setTaxaAcerto(calculaPorcentagemAcerto());
        Log.i("TAXA_ACERTO", "Taxa acerto foi de "+ sessao.getTaxaAcerto());
        Objects.requireNonNull(teste.getValue()).getSessoes().add(sessao);
        ExperimentoViewModel.updateTeste(Integer.parseInt(teste.getValue().getId()), teste.getValue().getSessoes());
    }

    private static int calculaPorcentagemAcerto(){
        int qtdEnsaios = sessao.getEnsaios().size();
        int qtdAcertos = 0;
        for (int i = 0; i < qtdEnsaios; i++){
            if(sessao.getEnsaios().get(i).getAcerto())
                qtdAcertos++;
        }
        double divisao = (double) qtdAcertos/qtdEnsaios;
        int porcentagem = (int) (divisao*100);
        Log.i("TAXA_ACERTO", " qtdAcerto = "+ qtdAcertos+ " qtdEnsaios = " +qtdEnsaios);
        return porcentagem;
    }
}
