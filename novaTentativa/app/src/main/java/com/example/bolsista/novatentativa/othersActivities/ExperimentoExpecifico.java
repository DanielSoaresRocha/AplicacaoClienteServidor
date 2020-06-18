package com.example.bolsista.novatentativa.othersActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.IniciarConfiguracao;
import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.TesteAdapter;
import com.example.bolsista.novatentativa.modelo.Experimento2;
import com.example.bolsista.novatentativa.recycleOnTouchLinesters.GenericOnItemTouch;

import java.util.Calendar;

import me.drakeet.materialdialog.MaterialDialog;

public class ExperimentoExpecifico extends AppCompatActivity {
    private Context contextActivity;
    private TextView nomeEquinoText, dataInicioText;
    private RecyclerView testesRecycleView;

    private Experimento2 experimento;
    private TesteAdapter adapter;
    private int POSITION_EXPERIMENTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experimento_expecifico);

        pegarPosicao();
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

        testesRecycleView.addOnItemTouchListener(
                new GenericOnItemTouch(
                        contextActivity,
                        testesRecycleView,
                        new GenericOnItemTouch.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                Intent it = new Intent(contextActivity, Sessoes.class);
                                it.putExtra("positionExperimento", POSITION_EXPERIMENTO);
                                it.putExtra("positionTeste", position);
                                it.putExtra("nomeTeste", experimento.getTestes()
                                        .get(position).getNome());
                                startActivity(it);
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onItemLongClick(View view, int position) {
                                View layout = LayoutInflater.from(contextActivity)
                                        .inflate(R.layout.configuracaoinformacao_inflater,null,false);

                                TextView detalhes = layout.findViewById(R.id.detalhesConfigInfo);
                                TextView intervalo1 = layout.findViewById(R.id.intervalo1ConfigInfo);
                                TextView intervalo2 = layout.findViewById(R.id.intervalo2ConfigInfo);
                                TextView qtdQuestoes = layout.findViewById(R.id.qtdQuestoesConfigInfo);

                                detalhes.setText(experimento.getTestes().get(position)
                                        .getDetalhes());
                                intervalo1.setText(experimento.getTestes().get(position)
                                        .getIntervalo1()+" segundos");
                                intervalo2.setText(experimento.getTestes().get(position)
                                        .getIntervalo2()+" segundos");
                                qtdQuestoes.setText(experimento.getTestes().get(position)
                                        .getQtdQuestoes()+" questões");

                                MaterialDialog m = new MaterialDialog(contextActivity)
                                        .setContentView(layout)
                                        .setCanceledOnTouchOutside(true);

                                m.show();
                            }
                        })
        );
    }

    private void pegarPosicao() {
        Intent it = getIntent();
        POSITION_EXPERIMENTO = it.getIntExtra("positionExperimento", 0);
        experimento = ExperimentosAndamento.experimentos2.get(POSITION_EXPERIMENTO);
    }

    private void inicializar() {
        nomeEquinoText = findViewById(R.id.nomeEquinoText);
        dataInicioText = findViewById(R.id.dataInicioText);
        testesRecycleView = findViewById(R.id.testesRecycleView);
        contextActivity = this;
    }
}
