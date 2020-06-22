package com.example.bolsista.novatentativa.othersActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.ExperimentoAdapter;
import com.example.bolsista.novatentativa.modelo.Configuracao;
import com.example.bolsista.novatentativa.modelo.Experimento2;
import com.example.bolsista.novatentativa.recycleOnTouchLinesters.GenericOnItemTouch;
import com.example.bolsista.novatentativa.viewsModels.ExperimentoViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class ExperimentosFinalizados extends AppCompatActivity {
    RecyclerView experimentosFRecycle;

    Context contextActivity;
    private ExperimentoAdapter adapter;

    public static ArrayList<Experimento2> experimentos2;

    //FireBase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usuarioRef;
    //Fire Base Auth
    FirebaseAuth usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experimentos_finalizados);

        usuario = FirebaseAuth.getInstance();
        usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());

        inicializar();
        preencher();
        implementsRecycle();
    }

    private void implementsRecycle(){
        adapter = new ExperimentoAdapter(contextActivity, experimentos2);
        experimentosFRecycle.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextActivity,LinearLayoutManager.VERTICAL,false);
        experimentosFRecycle.setLayoutManager(layout);

        experimentosFRecycle.addOnItemTouchListener(
                new GenericOnItemTouch(
                        contextActivity,
                        experimentosFRecycle,
                        new GenericOnItemTouch.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                Intent it = new Intent(contextActivity, ExperimentoExpecifico.class);
                                it.putExtra("positionExperimento", position);
                                startActivity(it);
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onItemLongClick(View view, int position) {
                                Toast.makeText(contextActivity, "Visualizar informações", Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }

    private void preencher() {
        ArrayList<Integer> imagens = new ArrayList<Integer>() {
            {
                add(1);
                add(2);
                add(3);
            }
        };

        ArrayList<Configuracao> testes = new ArrayList<>();

        testes.add(new Configuracao("id1", "Pré-teste",
                "Teste gabor", imagens, 10, 5, 5,
                234, 435, null,true));
        testes.add(new Configuracao("id2", "Teste de aprendizagem L1",
                "Teste gabor", imagens, 20, 5, 5, 234,
                435, null,true));
        testes.add(new Configuracao("id3", "Teste de aprendizagem L2",
                "Teste gabor", imagens, 15, 5, 5, 234,
                435, null,true));
        testes.add(new Configuracao("id4", "Teste de aprendizagem L3",
                "Teste gabor", imagens, 15, 5, 5, 234,
                435, null,true));
        testes.add(new Configuracao("id5", "Teste de transferência T1",
                "Teste gabor", imagens, 15, 5, 5, 234,
                435, null,true));
        testes.add(new Configuracao("id6", "Teste de transferência T2",
                "Teste gabor", imagens, 15, 5, 5, 234,
                435, null,true));

        experimentos2.add(new Experimento2("id1", "cavalo 1", "experimento 0", new Date(),
                new Date(), testes));

        ExperimentoViewModel.experimentos.setValue(experimentos2);
    }

    private void inicializar() {
        experimentosFRecycle = findViewById(R.id.experimentosFRecycle);
        contextActivity = this;

        experimentos2 = new ArrayList<>();

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.exp_finalizado);
    }
}
