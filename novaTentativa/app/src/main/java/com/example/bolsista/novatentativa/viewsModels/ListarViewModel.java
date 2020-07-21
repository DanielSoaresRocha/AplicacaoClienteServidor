package com.example.bolsista.novatentativa.viewsModels;

import com.example.bolsista.novatentativa.modelo.Equino;
import com.example.bolsista.novatentativa.modelo.Teste;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListarViewModel  extends ViewModel {
    public static MutableLiveData<ArrayList<Equino>> cavalos;
    public static MutableLiveData<ArrayList<Teste>> configuracoes;

    public ListarViewModel(){
    }

    public static void carregarListas(){
        cavalos = new MutableLiveData<>();
        configuracoes = new MutableLiveData<>();

        if(cavalos.getValue() == null){
            cavalos.setValue(new ArrayList<>());
            configuracoes.setValue(new ArrayList<>());
        }
    }

    public static void addCavalo(Equino equino){
        cavalos.getValue().add(equino);
        cavalos.setValue(cavalos.getValue());
    }

    public static void addConfiguracao(Teste teste) {
        configuracoes.getValue().add(teste);
        configuracoes.setValue(configuracoes.getValue());
    }
}
