package com.example.bolsista.novatentativa.viewsModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bolsista.novatentativa.modelo.Experimento2;

import java.util.ArrayList;

public class ExperimentoViewModel extends ViewModel {
    public static MutableLiveData<ArrayList<Experimento2>> experimentos;

    public ExperimentoViewModel(){
    }

    public static void carregarLista(){
        experimentos = new MutableLiveData<>();
        experimentos.setValue(new ArrayList<>());
    }


    public void setExperimento(ArrayList<Experimento2> exp){
        experimentos.setValue(exp);
    }
}
