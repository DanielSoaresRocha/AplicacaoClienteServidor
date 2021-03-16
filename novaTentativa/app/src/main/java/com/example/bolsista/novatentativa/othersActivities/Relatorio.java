package com.example.bolsista.novatentativa.othersActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Equino;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.example.bolsista.novatentativa.modelo.Teste;
import com.example.bolsista.novatentativa.viewsModels.ExperimentoViewModel;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Relatorio extends AppCompatActivity {
    private Experimento experimento;
    private Button relatorio1;
    public static final String DEST = "/sdcard/teste.pdf";

    private PdfFont bold;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        
        pegarExperimento();
        inicializar();
        listener();
    }

    private void listener() {
        relatorio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    File file = new File(DEST);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    file.getParentFile().mkdirs();
                    gerarPDF(DEST);
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void gerarPDF(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4);

        addTitleCenter(doc, "Relatório de um teste dentro de um experimento");
        breakLine(doc);

        Equino equino = experimento.getEquino();
        addParagraphInfo(doc, "Nome:", equino.getNome());
        addParagraphInfo(doc, "Idade:", calculaIdade(equino.getDataNascimento()).toString());
        addParagraphInfo(doc, "Raça:", equino.getRaca());
        addParagraphInfo(doc, "Atividade do equino:", equino.getAtividade());
        addParagraphInfo(doc, "Sexo:", equino.getSexo());
        addParagraphInfo(doc, "Observações:", equino.getObservacoes());
        addParagraphInfo(doc, "Sistema de criação:", equino.getSistemaCriacao());
        addParagraphInfo(doc, "Nível de atividade/trabalho semanal:", equino.getAtividadeSemanal());
        addParagraphInfo(doc, "Intensidade da atividade:", equino.getItensidadeAtividade());
        addParagraphInfo(doc, "Suplementação Mineral?", equino.isSuplementacaoMineral() ? "Sim" : " Não");
        addParagraphInfo(doc, "Temperamento:", equino.getTemperamento());
        addParagraphInfo(doc, "Classificação do Temperamento:", equino.getTemperamento());
        addParagraphInfo(doc, "Alguma Exteriotipia/vício?", equino.isVicio() ? "Sim" : "Não");
        addParagraphInfo(doc, "Problema de saúde nos últimnos 60 dias?", equino.getIsProblemaSaude() ? "Sim" : "Não");

        if(equino.getIsProblemaSaude()){
            addParagraphInfo(doc, "Qual?", equino.getProblemaSaude());
        }

        int caunter = 0;
        for(Teste teste : experimento.getTestes()){
            caunter++;

            breakLine(doc);
            addTitleCenter(doc, teste.getNome());
            breakLine(doc);

            float[] columnWidths = {1, 3, 3, 3, 3, 3};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));

            Cell cell = new Cell(1, 6)
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
                            .add(new Paragraph("Horário Inicial")),
                    new Cell()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Horário Final")),
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
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(String.valueOf(caunter))));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(formatarData(sessao.getData()))));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("6")));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph("6")));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(Integer.toString(sessao.getTaxaAcerto()) + "%")));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(sessao.getExperimentador().getNome())));

                caunter++;
            }
            caunter = 0;

            doc.add(table);
            doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE)); // Quebrar página
        }

        doc.close();
    }

    // Quebrar uma linha no PDF
    private void breakLine(Document doc) {
        Text text = new Text("\n");

        Paragraph paragraph = new Paragraph()
                .add(text);

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

    private void addParagraphInfo(Document doc, String information, String text) throws IOException{
        Text info = new Text(information + "  ").setFont(bold);
        Text txt = new Text(text);

        Paragraph paragraph = new Paragraph().add(info).add(txt);

        doc.add(paragraph);
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

    private void inicializar() {
        relatorio1 = findViewById(R.id.relatorio1);
    }

    private void pegarExperimento() {
        experimento = (Experimento) getIntent().getSerializableExtra("experimento");
        ExperimentoViewModel.experimento.setValue(experimento);

        try {
            bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}