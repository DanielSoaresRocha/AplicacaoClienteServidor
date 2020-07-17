package com.example.bolsista.novatentativa.graficos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import java.util.ArrayList;

public class GraficoBarra extends AppCompatActivity {
    Graficos g;
    BarChart graficoBarra;
    ArrayList<Sessao> sessoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_barra);

        getSessoes();
        inicializar();
        layoutGraficoBarra();

        g.getGraficoBarras(graficoBarra, sessoes);
    }

    private void getSessoes() {
        sessoes = (ArrayList<Sessao>) getIntent().getSerializableExtra("sessoes");
        Log.i("SESSOES", "ARRAYLIST = " + sessoes.get(0).getNome() + " " + sessoes.get(0).getTaxaAcerto());
    }

    private void layoutGraficoBarra() {
        Legend legend = graficoBarra.getLegend();
        legend.setTextSize(15f);

        XAxis xAxis = graficoBarra.getXAxis(); // retorna o eixo 'x' do grafico graficoLinha.get...
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);// BOTTOM -> eixo X na parte inferior do gráfico
        xAxis.setAxisMinimum(0f);// valor inicial mínimo - eixo 'x'
        //xAxis.setAxisMaximum(30f);// valor inicial máximo - eixo 'x'
        xAxis.setGranularity(1f);// intervalo mínimo entre os valores - eixo 'x'

        YAxis yAxis = graficoBarra.getAxisLeft();// retorna o eixo Esquerdo 'y' do grafico graficoLinha.get...
        YAxis yAxis1 = graficoBarra.getAxisRight();
        //yAxis.setGranularity(10f);
        yAxis.setAxisMinimum(0);
        yAxis1.setEnabled(false);// Eixo da Direita não é exibido se 'false'

        graficoBarra.setDragEnabled(true);
        graficoBarra.setScaleEnabled(false);
        graficoBarra.setPinchZoom(true);// Se 'false' -> ativa zoom para eixo 'x' e 'y' SEPARADAMENTE
        graficoBarra.setScaleEnabled(true);// Se 'true' ativa as escalas dos eixos
        //graficoBarra.setVisibleXRangeMaximum(10f);// valor inicial mínimo - eixo 'x' - Visível
    }

    private void inicializar() {
        g = new Graficos();
        graficoBarra = (BarChart) findViewById(R.id.graficoBarra);
    }
}