package com.example.bolsista.novatentativa.viewsModels;

import com.example.bolsista.novatentativa.modelo.Equino;
import com.example.bolsista.novatentativa.modelo.Configuracao;
import com.example.bolsista.novatentativa.modelo.Experimento;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListarViewModel  extends ViewModel {
    public static MutableLiveData<ArrayList<Equino>> cavalos;
    public static MutableLiveData<ArrayList<Configuracao>> configuracoes;
    public static MutableLiveData<ArrayList<Experimento>> experimentos;

    public ListarViewModel(){
    }

    public static void carregarListas(){
        cavalos = new MutableLiveData<>();
        configuracoes = new MutableLiveData<>();
        experimentos = new MutableLiveData<>();

        if(cavalos.getValue() == null){
            cavalos.setValue(new ArrayList<>());
            configuracoes.setValue(new ArrayList<>());
            experimentos.setValue(new ArrayList<>());
        }
    }

    public static void addCavalo(Equino cavalo){
        cavalos.getValue().add(cavalo);
        cavalos.setValue(cavalos.getValue());
    }

    public static void addConfiguracao(Configuracao configuracao) {
        configuracoes.getValue().add(configuracao);
        configuracoes.setValue(configuracoes.getValue());
    }

    public static void addExperimento(Experimento experimento) {
        experimentos.getValue().add(experimento);
        experimentos.setValue(experimentos.getValue());
    }
}
