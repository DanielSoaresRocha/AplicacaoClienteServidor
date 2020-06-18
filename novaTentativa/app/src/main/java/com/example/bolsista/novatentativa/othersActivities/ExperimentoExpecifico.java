package com.example.bolsista.novatentativa.othersActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.TesteAdapter;
import com.example.bolsista.novatentativa.modelo.Experimento2;

import java.util.Calendar;

public class ExperimentoExpecifico extends AppCompatActivity {
    private Context contextActivity;
    private TextView nomeEquinoText, dataInicioText;
    private RecyclerView testesRecycleView;

    private Experimento2 experimento;
    private TesteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experimento_expecifico);

        pegarId();
        inicializar();
        preencher();
        implementsRecycle();
    }

    @SuppressLint("SetTextI18n")
    private void preencher() {
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(experimento.getNome());

        nomeEquinoText.setText(experimento.getEquino());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(experimento.getDataInicio());
        dataInicioText.setText(calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)
        +"/"+calendar.get(Calendar.YEAR));
    }

    private void implementsRecycle() {
        adapter = new TesteAdapter(contextActivity, experimento.getTestes(),true);
        testesRecycleView.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextActivity, LinearLayoutManager.VERTICAL, false);
        testesRecycleView.setLayoutManager(layout);
    }

    private void pegarId() {
        Intent it = getIntent();
        int id = it.getIntExtra("idExperimento", 0);
        experimento = ExperimentosAndamento.experimentos2.get(id);
    }

    private void inicializar() {
        nomeEquinoText = findViewById(R.id.nomeEquinoText);
        dataInicioText = findViewById(R.id.dataInicioText);
        testesRecycleView = findViewById(R.id.testesRecycleView);
        contextActivity = this;
    }
}
