package com.example.bolsista.novatentativa.viewsModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.modelo.Sessao;

import java.util.ArrayList;
import java.util.Objects;

public class ExperimentoViewModel extends ViewModel {
    public static MutableLiveData<ArrayList<Experimento>> experimentos;
    public static MutableLiveData<Experimento> experimento;

    public ExperimentoViewModel(){
    }

    public static void carregarLista(){
        experimentos = new MutableLiveData<>();
        experimentos.setValue(new ArrayList<>());
        experimento = new MutableLiveData<>();
    }

    public void setExperimento(ArrayList<Experimento> exp){
        experimentos.setValue(exp);
    }

    public static void updateTeste(int numTeste, ArrayList<Sessao> sessoes){
        Objects.requireNonNull(experimento.getValue()).getTestes().get(numTeste).setSessoes(sessoes);
    }
}
