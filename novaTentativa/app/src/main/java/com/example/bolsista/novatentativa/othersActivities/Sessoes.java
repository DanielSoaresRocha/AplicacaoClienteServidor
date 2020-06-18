package com.example.bolsista.novatentativa.othersActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.SessaoAdapter;
import com.example.bolsista.novatentativa.adapters.TesteAdapter;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.example.bolsista.novatentativa.modelo.Usuario;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class Sessoes extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private RecyclerView sessaoRecycleView;
    private Spinner experimentadorSpinner;

    private int POSITION_EXPERIMENTO;
    private int POSITION_TESTE;
    private ArrayList<Sessao> sessoes = new ArrayList<>();
    private SessaoAdapter adapter;
    ArrayAdapter<CharSequence> sessoesAdapter;
    Context contextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessoes);

        pegarPosicao();
        preencherArray();
        inicializar();
        implementsRecycle();
        listener();
        spinner();
    }

    private void spinner() {
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios.add(new Usuario("id1", "Daniel"));
        usuarios.add(new Usuario("id1", "Mário"));
        usuarios.add(new Usuario("id1", "Leonardo"));
        usuarios.add(new Usuario("id1", "Marcos"));
        usuarios.add(new Usuario("id1", "Beatriz"));

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, usuarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        experimentadorSpinner.setAdapter(adapter);
    }

    private void listener() {
        experimentadorSpinner.setOnItemSelectedListener(this);
    }

    private void preencherArray() {
        sessoes.add(new Sessao("id1", "Sessao 1", new Date(), 25,
                new Usuario("id1", "Daniel")));
        sessoes.add(new Sessao("id2", "Sessao 2", new Date(), 40,
                new Usuario("id2", "Roberto")));
        sessoes.add(new Sessao("id3", "Sessao 3", new Date(), 65,
                new Usuario("id3", "Sara")));
        sessoes.add(new Sessao("id4", "Sessao 4", new Date(), 80,
                new Usuario("id4", "Cristina")));
        sessoes.add(new Sessao("id5", "Sessao 5", new Date(), 95,
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
        experimentadorSpinner = findViewById(R.id.experimentadorSpinner);
        contextActivity = this;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
