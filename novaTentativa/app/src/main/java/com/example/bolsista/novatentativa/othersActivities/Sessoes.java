package com.example.bolsista.novatentativa.othersActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.SessaoAdapter;
import com.example.bolsista.novatentativa.adapters.TesteAdapter;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.example.bolsista.novatentativa.modelo.Usuario;

import java.util.ArrayList;
import java.util.Date;

public class Sessoes extends AppCompatActivity {
    private RecyclerView sessaoRecycleView;

    private int POSITION_EXPERIMENTO;
    private int POSITION_TESTE;
    private ArrayList<Sessao> sessoes = new ArrayList<>();
    private SessaoAdapter adapter;
    Context contextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessoes);

        pegarPosicao();
        preencherArray();
        inicializar();
        implementsRecycle();
    }

    private void preencherArray() {
        sessoes.add(new Sessao("id1", "Sessao 1", new Date(), 20,
                new Usuario("id1", "Daniel")));
        sessoes.add(new Sessao("id2", "Sessao 2", new Date(), 20,
                new Usuario("id2", "Roberto")));
        sessoes.add(new Sessao("id3", "Sessao 3", new Date(), 20,
                new Usuario("id3", "Sara")));
        sessoes.add(new Sessao("id4", "Sessao 4", new Date(), 20,
                new Usuario("id4", "Cristina")));
        sessoes.add(new Sessao("id5", "Sessao 5", new Date(), 20,
                new Usuario("id6", "Lucas")));
    }

    private void implementsRecycle() {
        adapter = new SessaoAdapter(contextActivity, sessoes);
        sessaoRecycleView.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextActivity, LinearLayoutManager.VERTICAL, false);
        sessaoRecycleView.setLayoutManager(layout);
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

    private void inicializar() {
        sessaoRecycleView = findViewById(R.id.sessaoRecycleView);
        contextActivity = this;
    }
}
