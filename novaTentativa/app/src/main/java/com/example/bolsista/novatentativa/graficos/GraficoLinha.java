package com.example.bolsista.novatentativa.graficos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import java.util.ArrayList;

public class GraficoLinha extends AppCompatActivity {
    LineChart graficoLinha;
    Graficos g;
    ArrayList<Sessao> sessoes;
    String nomeEquino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_linha);

        getSessoes();
        inicializar();
        layoutGraficoLinha();

        g.getGraficoLinha(graficoLinha,sessoes, nomeEquino);
    }

    private void getSessoes() {
        sessoes = (ArrayList<Sessao>) getIntent().getSerializableExtra("sessoes");
        nomeEquino = getIntent().getStringExtra("nomeEquino");
    }

    private void layoutGraficoLinha() {
        Legend legend = graficoLinha.getLegend();
        legend.setTextSize(15f);

        XAxis xAxis = graficoLinha.getXAxis(); // retorna o eixo 'x' do grafico graficoLinha.get...
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);// BOTTOM -> eixo X na parte inferior do gráfico
        xAxis.setAxisMinimum(0f);// valor inicial mínimo - eixo 'x'
        //xAxis.setAxisMaximum(30f);// valor inicial máximo - eixo 'x'
        xAxis.setGranularity(1f);// intervalo mínimo entre os valores - eixo 'x'

        YAxis yAxis = graficoLinha.getAxisLeft();// retorna o eixo Esquerdo 'y' do grafico graficoLinha.get...
        YAxis yAxis1 = graficoLinha.getAxisRight();
        //yAxis.setGranularity(10f);
        yAxis.setAxisMinimum(0);
        yAxis1.setEnabled(false);// Eixo da Direita não é exibido se 'false'

        graficoLinha.setDragEnabled(true);
        graficoLinha.setScaleEnabled(false);
        graficoLinha.setPinchZoom(true);// Se 'false' -> ativa zoom para eixo 'x' e 'y' SEPARADAMENTE
        graficoLinha.setScaleEnabled(true);// Se 'true' ativa as escalas dos eixos
        //graficoLinha.setVisibleXRangeMaximum(10f);// valor inicial mínimo - eixo 'x' - Visível
    }

    private void inicializar() {
        g = new Graficos();
        graficoLinha = (LineChart) findViewById(R.id.graficoLinha);
    }
}