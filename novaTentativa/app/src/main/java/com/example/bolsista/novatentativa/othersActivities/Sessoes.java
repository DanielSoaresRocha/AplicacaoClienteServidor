package com.example.bolsista.novatentativa.othersActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Sessao;

import java.util.ArrayList;

public class Sessoes extends AppCompatActivity {
    private int POSITION_EXPERIMENTO;
    private int POSITION_TESTE;

    private ArrayList<Sessao> sessoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessoes);

        pegarPosicao();
    }

    private void pegarPosicao() {
        Intent it = getIntent();
        POSITION_EXPERIMENTO = it.getIntExtra("positionExperimento", 0);
        POSITION_TESTE = it.getIntExtra("positionTeste", 0);
        POSITION_TESTE = it.getIntExtra("positionTeste", 0);

        // mudar Titulo da barra
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(it.getStringExtra("nomeTeste"));
        /*
        sessoes = ExperimentosAndamento.experimentos2.get(POSITION_EXPERIMENTO)
                .getTestes().get(POSITION_TESTE)
                .getSessoes();*/
    }
}
