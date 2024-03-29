package com.example.bolsista.novatentativa.othersActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.TesteAdapter;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.recycleOnTouchLinesters.GenericOnItemTouch;
import com.example.bolsista.novatentativa.viewsModels.ExperimentoViewModel;

import java.util.Calendar;

import me.drakeet.materialdialog.MaterialDialog;

public class ExperimentoExpecifico extends AppCompatActivity {
    private Context contextActivity;
    private TextView nomeEquinoText, dataInicioText;
    private RecyclerView testesRecycleView;

    private Experimento experimento;
    private TesteAdapter adapter;
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
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(experimento.getNome());

        nomeEquinoText.setText(experimento.getEquino().getNome());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(experimento.getDataInicio());
        dataInicioText.setText(calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)
        +"/"+calendar.get(Calendar.YEAR));
    }

    private void implementsRecycle() {
        adapter = new TesteAdapter(contextActivity, experimento.getTestes(),true, null);
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
                                it.putExtra("teste", experimento.getTestes().get(position));
                                it.putExtra("aleatoriedade", experimento.getTestes().get(position).getAleatoriedade());
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
                                        .getMaxVezesConsecutivas()+ " vezes consecutivas");
                                intervalo1.setText(experimento.getTestes().get(position)
                                        .getIntervalo1()+" segundos");
                                intervalo2.setText(experimento.getTestes().get(position)
                                        .getIntervalo2()+" segundos");
                                qtdQuestoes.setText(experimento.getTestes().get(position)
                                        .getQtdQuestoesPorSessao()+" ensaios");

                                MaterialDialog m = new MaterialDialog(contextActivity)
                                        .setContentView(layout)
                                        .setCanceledOnTouchOutside(true);

                                m.show();
                            }
                        })
        );
    }

    private void pegarPosicao() {
        experimento = (Experimento) getIntent().getSerializableExtra("experimento");
        ExperimentoViewModel.experimento.setValue(experimento);
    }

    private void inicializar() {
        nomeEquinoText = findViewById(R.id.nomeEquinoText);
        dataInicioText = findViewById(R.id.dataInicioText);
        testesRecycleView = findViewById(R.id.testesRecycleView);
        contextActivity = this;
    }

    /*
    private void observerList() {
        ExperimentoViewModel.experimentos.observe(this, experimento2s -> {
            // update UI

        });

        mViewModel.cavalos.observe(this, clientes -> {
            // update UI
            adapter.notifyDataSetChanged();
        });
    }*/
}
