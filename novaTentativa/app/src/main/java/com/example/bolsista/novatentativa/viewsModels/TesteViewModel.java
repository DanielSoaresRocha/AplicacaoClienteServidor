package com.example.bolsista.novatentativa.viewsModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

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
        Objects.requireNonNull(teste.getValue()).getSessoes().add(sessao);


        Log.i("InicioSessao", "QtdDeSessoes no teste:" + teste.getValue().getSessoes().size());
        Log.i("InicioSessao", "QtdEnsaios na sessao:" + teste.getValue().getSessoes().get(0).getEnsaios().size());

    }
}
