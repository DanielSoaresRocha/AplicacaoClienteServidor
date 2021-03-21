package com.example.bolsista.novatentativa.othersActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.EnsaioAdapter;
import com.example.bolsista.novatentativa.adapters.TesteAdapter;
import com.example.bolsista.novatentativa.modelo.Ensaio;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.example.bolsista.novatentativa.recycleOnTouchLinesters.GenericOnItemTouch;
import com.example.bolsista.novatentativa.viewsModels.ExperimentoViewModel;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class SessaoExpecifica extends AppCompatActivity {
    private Context contextActivity;
    private TextView nomeSessaoText, horaInicioText;
    private RecyclerView ensaiosRecycleView;

    private Sessao sessao;
    private EnsaioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessao_expecifica);

        pegarSessao();
        inicializar();
        preencher();
        implementsRecycle();
    }

    private void implementsRecycle() {
        adapter = new EnsaioAdapter(contextActivity, sessao.getEnsaios());
        ensaiosRecycleView.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextActivity, LinearLayoutManager.VERTICAL, false);
        ensaiosRecycleView.setLayoutManager(layout);
    }

    private void preencher() {
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(sessao.getNome());

        nomeSessaoText.setText(sessao.getNome());

        Calendar calendar = Calendar.getInstance(new Locale("BR"));
        calendar.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        calendar.setTime(sessao.getData());
        horaInicioText.setText(calendar.get(Calendar.HOUR) + ":"+ calendar.get(Calendar.MINUTE));
    }

    private void inicializar() {
        nomeSessaoText = findViewById(R.id.nomeSessaoText);
        horaInicioText = findViewById(R.id.horaInicioText);
        ensaiosRecycleView = findViewById(R.id.ensaiosRecycleView);
        contextActivity = this;
    }

    private void pegarSessao() {
        sessao = (Sessao) getIntent().getSerializableExtra("sessao");
    }
}