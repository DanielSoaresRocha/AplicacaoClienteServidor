package com.example.bolsista.novatentativa.viewsModels;

import androidx.lifecycle.MutableLiveData;

import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.modelo.Teste;

import java.util.ArrayList;

public class TesteViewModel {
    public static MutableLiveData<Teste> teste;

    public static void iniciarTeste(Teste testeDaVez){
        teste = new MutableLiveData<>();
        teste.setValue(testeDaVez);
    }
}
