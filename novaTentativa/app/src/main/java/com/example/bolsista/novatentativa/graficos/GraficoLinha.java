package com.example.bolsista.novatentativa.graficos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.File;
import java.util.ArrayList;

public class GraficoLinha extends AppCompatActivity {
    LineChart graficoLinha;
    Graficos g;
    ArrayList<Sessao> sessoes;
    String nomeEquino;
    Button gerarGrafico;

    private final int REQUEST_CODE = 3434;
    public static final String DEST = "/sdcard/teste.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_linha);

        getSessoes();
        inicializar();
        layoutGraficoLinha();

        g.getGraficoLinha(graficoLinha,sessoes, nomeEquino);
        listener();
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

    private void listener(){
        gerarGrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    File file = new File(DEST);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    file.getParentFile().mkdirs();
                    manipulatePdf(DEST);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4.rotate());

        graficoLinha.buildDrawingCache();
        Bitmap bm = graficoLinha.getDrawingCache();

        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream3);
        byte[] byteArray = stream3.toByteArray();

        Image gray = new Image(ImageDataFactory.create(byteArray));
        gray.scaleToFit(1000, 500);

        doc.add(gray);

        doc.close();
        Toast.makeText(this, DEST+"is saved to", Toast.LENGTH_SHORT).show();
    }

    private void inicializar() {
        g = new Graficos();
        graficoLinha = (LineChart) findViewById(R.id.graficoLinha);
        gerarGrafico = findViewById(R.id.gerarGrafico);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

    }
}