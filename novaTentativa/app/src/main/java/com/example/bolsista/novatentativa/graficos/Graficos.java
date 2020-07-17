package com.example.bolsista.novatentativa.graficos;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Graficos {

    ArrayList<BarEntry> entradas;
    ArrayList<Entry> entradasL;
    ArrayList<Sessao> sessoes;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    float valX[] = new float[100]; // declaração combinada
    float valY[] = new float[100];

    public Graficos(){

    }

    public void getGraficoBarras(final BarChart graficoBarra,ArrayList<Sessao> sessoes){
        entradas = new ArrayList<BarEntry>();
        this.sessoes = sessoes;

        BarData temp = new BarData(addEntradasBar());
        temp.setDrawValues(false);
        temp.setValueTextSize(15f);
        temp.setBarWidth(1f);

        graficoBarra.setData(temp);// responsável por plotar os gráficos
        graficoBarra.invalidate(); // refresh
    }

    public void getGraficoLinha(final LineChart graficoLinha){

        entradasL = new ArrayList<Entry>();
        sessoes = new ArrayList<Sessao>();

        db.collection("sessoes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                sessoes.add((Sessao) document.toObject(Sessao.class));

                                Log.i("FireStore", document.getId() + " => " + document.getData());
                            }

                        } else {
                            Log.i("FireStore", "Error getting documents: ", task.getException());
                        }

                        LineData data = new LineData(addEntradasLin()); // Objeto responsável por receber os valores dos dados/ Objeto usado para plotar os gráficos
                        data.setDrawValues(false);
                        data.setValueTextSize(15f);

                        graficoLinha.setData(data);
                        graficoLinha.invalidate();
                        System.out.println("A THREAD FINALIZOU SUA EXECUÇÃO");
                    }

                });
    }

    private BarDataSet addEntradasBar(){
        for(int i = 0; i < sessoes.size(); i++) {
            valX[i] = Integer.parseInt(sessoes.get(i).getId())+1;
            valY[i] = sessoes.get(i).getTaxaAcerto();
            //System.out.println(valX[i]+" "+valY[i]);
            entradas.add(new BarEntry(valX[i], valY[i]));
        }

        BarDataSet barDataSet1 = new BarDataSet(entradas, "Bennie");
        barDataSet1.setColor(Color.BLUE);
        barDataSet1.setBarBorderWidth(2f);

        return barDataSet1;
    }
//Classe estatística getMédia - da taxa de acerto
// e getMediana - medianas - da taxa de acerto
    private LineDataSet addEntradasLin(){
        for (int i = 0; i < sessoes.size(); i++) {
            valX[i] = /*sessoes.get(i).getIdSessao();*/i;
            valY[i] = sessoes.get(i).getTaxaAcerto();
        }
        ordena();
        for (int i = 0; i < sessoes.size(); i++) {
            entradasL.add(new Entry(valX[i], valY[i]));
        }
        LineDataSet linEntradas1 = new LineDataSet(entradasL, "Bennie");
        linEntradas1.setColor(Color.BLUE);
        linEntradas1.setLineWidth(5f);

        return linEntradas1;
    }

    private void ordena(){
        for (int i = 0; i < sessoes.size(); i++)
        {
            for (int j = 0; j < sessoes.size(); j++)
            {
                if (valX[i] < valX[j])
                {
                    //aqui acontece a troca. O maior vai para a direita e o menor para a esquerda
                    float aux = valX[i];
                    float auxY = valY[i];

                    valX[i] = valX[j];
                    valY[i] = valY[j];

                    valX[j] = aux;
                    valY[j] = auxY;
                }
            }
        }
    }//Termina método
}

