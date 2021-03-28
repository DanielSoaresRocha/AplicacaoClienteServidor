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
import com.example.bolsista.novatentativa.modelo.Ensaio;
import com.example.bolsista.novatentativa.modelo.Equino;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.example.bolsista.novatentativa.modelo.Teste;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class GraficoLinha extends AppCompatActivity {
    LineChart graficoLinha;
    Graficos g;
    Experimento experimento;
    Teste teste;

    Button gerarGrafico;

    private final int REQUEST_CODE = 3434;
    String DEST = "/sdcard/";

    private PdfFont bold;

    DecimalFormat formato = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_linha);

        getSessoes();
        inicializar();
        layoutGraficoLinha();

        g.getGraficoLinha(graficoLinha, teste.getSessoes(), experimento.getEquino().getNome());
        listener();
    }

    private void getSessoes() {
        teste = (Teste) getIntent().getSerializableExtra("teste");
        experimento = (Experimento) getIntent().getSerializableExtra("experimento");
    }

    private void layoutGraficoLinha() {
        Legend legend = graficoLinha.getLegend();
        legend.setTextSize(15f);

        XAxis xAxis = graficoLinha.getXAxis(); // retorna o eixo 'x' do grafico graficoLinha.get...
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);// BOTTOM -> eixo X na parte inferior do gráfico
        xAxis.setAxisMinimum(1f);// valor inicial mínimo - eixo 'x'
        //xAxis.setAxisMaximum(30f);// valor inicial máximo - eixo 'x'
        xAxis.setGranularity(1f);// intervalo mínimo entre os valores - eixo 'x'

        YAxis yAxis = graficoLinha.getAxisLeft();// retorna o eixo Esquerdo 'y' do grafico graficoLinha.get...
        YAxis yAxis1 = graficoLinha.getAxisRight();
        //yAxis.setGranularity(10f);
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(100);
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
        Document doc = new Document(pdfDoc, PageSize.A4);

        addTitleCenter(doc, "Relatório de um teste dentro de um experimento");
        breakLine(doc);

        Equino equino = experimento.getEquino();
        addParagraphInfo(doc, "Nome do Equino:", equino.getNome());
        addParagraphInfo(doc, "Idade:", calculaIdade(equino.getDataNascimento()).toString());
        addParagraphInfo(doc, "Raça:", equino.getRaca());
        addParagraphInfo(doc, "Atividade do equino:", equino.getAtividade());
        addParagraphInfo(doc, "Sexo:", equino.getSexo());
        addParagraphInfo(doc, "Observações:", equino.getObservacoes());
        addParagraphInfo(doc, "Sistema de criação:", equino.getSistemaCriacao());
        addParagraphInfo(doc, "Nível de atividade/trabalho semanal:", equino.getAtividadeSemanal());
        addParagraphInfo(doc, "Intensidade da atividade:", equino.getItensidadeAtividade());
        addParagraphInfo(doc, "Suplementação Mineral?", equino.isSuplementacaoMineral() ? "Sim" : " Não");
        addParagraphInfo(doc, "Classificação do Temperamento:", equino.getTemperamento());
        addParagraphInfo(doc, "Alguma Exteriotipia/vício?", equino.isVicio() ? "Sim" : "Não");
        addParagraphInfo(doc, "Problema de saúde nos últimnos 60 dias?", equino.getIsProblemaSaude() ? "Sim" : "Não");

        if(equino.getIsProblemaSaude()){
            addParagraphInfo(doc, "Qual?", equino.getProblemaSaude());
        }

        int caunter = 0;

        breakLine(doc);
        addTitleCenter(doc, teste.getNome());
        breakLine(doc);

        // Tabela
        float[] columnWidths = {3, 3, 3, 3, 4};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));

        Cell cell = new Cell(1, 5)
                .add(new Paragraph("Sessões"))
                .setFontSize(13)
                .setFontColor(DeviceGray.WHITE)
                .setBackgroundColor(DeviceGray.BLACK)
                .setTextAlignment(TextAlignment.CENTER);

        table.addHeaderCell(cell);

        Cell[] headerFooter = new Cell[]{
                new Cell()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(new DeviceGray(0.75f))
                        .add(new Paragraph("Sessão")),
                new Cell()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(new DeviceGray(0.75f))
                        .add(new Paragraph("Data")),
                new Cell()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(new DeviceGray(0.75f))
                        .add(new Paragraph("Tempo Gasto/Minutos")),
                new Cell()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(new DeviceGray(0.75f))
                        .add(new Paragraph("Taxa de acerto")),
                new Cell()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBackgroundColor(new DeviceGray(0.75f))
                        .add(new Paragraph("Experimentador"))
        };

        for (Cell hfCell : headerFooter) {
            table.addHeaderCell(hfCell);
        }

        for(Sessao sessao : teste.getSessoes()){
            caunter++;
            double tempoMillis = 0;
            for(Ensaio ensaio : sessao.getEnsaios()){
                tempoMillis += ensaio.getTempoAcerto();
            }

            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                    .add(new Paragraph(String.valueOf(caunter))));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                    .add(new Paragraph(formatarData(sessao.getData()))));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                    .add(new Paragraph(millisParaMinutos(tempoMillis))));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                    .add(new Paragraph(Integer.toString(sessao.getTaxaAcerto()) + "%")));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                    .add(new Paragraph(sessao.getExperimentador().getNome())));
        }
        caunter = 0;

        doc.add(table);

        graficoLinha.buildDrawingCache();
        Bitmap bm = graficoLinha.getDrawingCache();

        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream3);
        byte[] byteArray = stream3.toByteArray();

        Image graphic = new Image(ImageDataFactory.create(byteArray));
        graphic.scaleToFit(1000, 500);
        graphic.setMarginLeft(85);

        doc.add(graphic);

        // Media e Mediana
        Estatistica estatistica = new Estatistica(teste.getSessoes());
        addParagraphInfo(doc, "Média:", estatistica.getMedia());
        addParagraphInfo(doc, "Mediana:", estatistica.getMediana());
        addParagraphInfo(doc, "Desvio Padrão:", estatistica.getDesvioPadrao());

        doc.close();
        Toast.makeText(this, DEST +" Relatório gerado", Toast.LENGTH_SHORT).show();
    }

    private String millisParaMinutos(double tempoMillis) {
        double tempoEmMinutos = (tempoMillis / 1000) / 60;

        return formato.format(tempoEmMinutos);
    }

    private String formatarData(Date data){
        Calendar cal = Calendar.getInstance(new Locale("BR"));
        cal.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        cal.setTime(data);

        String dataFormatada = cal.get(Calendar.DAY_OF_MONTH) + "/" +(cal.get(Calendar.MONTH)+1) + "/" +cal.get(Calendar.YEAR);

        return dataFormatada;
    }

    public Integer calculaIdade(Date data) {
        GregorianCalendar hj = new GregorianCalendar();
        GregorianCalendar nascimento=new GregorianCalendar();

        if(data != null){
            nascimento.setTime(data);
        }

        int anohj = hj.get(Calendar.YEAR);
        int anoNascimento=nascimento.get(Calendar.YEAR);

        return new Integer(anohj-anoNascimento);
    }

    private void addParagraphInfo(Document doc, String information, String text) throws IOException{
        Text info = new Text(information + "  ").setFont(bold);
        Text txt = new Text(text);

        Paragraph paragraph = new Paragraph().add(info).add(txt).setTextAlignment(TextAlignment.LEFT);

        doc.add(paragraph);
    }

    private void addTitleCenter(Document doc, String text){
        Text txt = new Text(text);

        Paragraph paragraph = new Paragraph()
                .add(txt)
                .setTextAlignment(TextAlignment.CENTER)
                .setFont(bold);

        doc.add(paragraph);
    }

    // Quebrar uma linha no PDF
    private void breakLine(Document doc) {
        Text text = new Text("\n");

        Paragraph paragraph = new Paragraph()
                .add(text);

        doc.add(paragraph);
    }

    private void inicializar() {
        g = new Graficos();
        graficoLinha = (LineChart) findViewById(R.id.graficoLinha);
        gerarGrafico = findViewById(R.id.gerarGrafico);

        DEST = DEST.concat("testeEspecifico.pdf");

        try {
            bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

    }
}