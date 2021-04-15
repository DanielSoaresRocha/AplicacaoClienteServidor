package com.example.bolsista.novatentativa.othersActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.ExperimentoAdapter;
import com.example.bolsista.novatentativa.modelo.Desafio;
import com.example.bolsista.novatentativa.modelo.Ensaio;
import com.example.bolsista.novatentativa.modelo.Equino;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.example.bolsista.novatentativa.modelo.Teste;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.modelo.Usuario;
import com.example.bolsista.novatentativa.recycleOnTouchLinesters.GenericOnItemTouch;
import com.example.bolsista.novatentativa.viewsModels.ExperimentoViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ExperimentosAndamento extends AppCompatActivity {
    private RecyclerView experimentosRecycle;
    private ProgressBar progressBarAndamento;

    Context contextActivity;
    private ExperimentoAdapter adapter;

    public static ArrayList<Experimento> experimentos;

    //FireBase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usuarioRef;
    //Fire Base Auth
    FirebaseAuth usuario;
    Random random = new Random(); // gerar número aleatório

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experimentos_andamento);

        usuario = FirebaseAuth.getInstance();
        usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());

        inicializar();
        getExperimentosFireStore();
    }

    private void getExperimentosFireStore() {
        db.collection("experimentos")
                //.whereEqualTo("users", usuarioRef)//referencia do usuario que adicionou o cavalo
                .whereEqualTo("finalizado", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Experimento experimento = document.toObject(Experimento.class);
                                //alterarExpTest(experimento);
                                experimentos.add(experimento);
                            }
                            implementsRecycle();
                        } else {
                            Log.i("DataBase-FireStore-get", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void alterarExpTest(Experimento experimento){
        int qtdSessoes = 0;
        experimento.setFinalizado(true);
        for(Teste teste : experimento.getTestes()){
            if(teste.getId().equals("0")) {
                qtdSessoes = 10;
            }else if(teste.getId().equals("1")) {
                qtdSessoes = 20;
            }else if(teste.getId().equals("2")) {
                qtdSessoes = 24;
            }else if(teste.getId().equals("3")) {
                qtdSessoes = 22;
            }else if(teste.getId().equals("4")){
                qtdSessoes = 17;
            }else{
                qtdSessoes = 14;
            }

            teste.setSessoes(new ArrayList<>());

            for(int j = 0; j < qtdSessoes; j++) {
                Sessao sessao = new Sessao();
                teste.getSessoes().add(sessao);
                teste.getSessoes().get(j).setEnsaios(new ArrayList<>());
                for (int i = 0; i < 10; i++) {
                    Ensaio ensaio = new Ensaio();
                    ensaio.setId("kasda");
                    boolean acerto = numeroAleatorio(0, 1) == 0;
                    if(j > (qtdSessoes-3) && i > 1)
                        acerto = true;
                    ensaio.setAcerto(acerto);
                    ensaio.setIdDesafio("dfasdf");
                    ensaio.setDesafio(new Desafio("adf", R.drawable.peace, R.drawable.peace, R.drawable.plane));
                    ensaio.setTempoAcerto(numeroAleatorio(120000, 360000));

                    teste.getSessoes().get(j).getEnsaios().add(ensaio);
                }
                teste.getSessoes().get(j).calculaPorcentagemAcerto();
                teste.getSessoes().get(j).setData(new Date());

                Usuario usuario = new Usuario();
                usuario.setUid("23423");
                usuario.setNome("Daniel Soares");
                teste.getSessoes().get(j).setExperimentador(usuario);
                teste.getSessoes().get(j).setNome("Sessão "+ (j+1));
                teste.getSessoes().get(j).setId(Integer.toString(j));
            }

        }

        experimento.setFinalizado(true);
    }

    private int numeroAleatorio(int min, int max) {
         // gerar número aleatório
        int numeroTmp = random.nextInt(max - (min - 1)) + min;
        return numeroTmp;
    }

    private void implementsRecycle(){
        adapter = new ExperimentoAdapter(contextActivity, experimentos);
        experimentosRecycle.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextActivity,LinearLayoutManager.VERTICAL,false);
        experimentosRecycle.setLayoutManager(layout);

        experimentosRecycle.addOnItemTouchListener(
                new GenericOnItemTouch(
                        contextActivity,
                        experimentosRecycle,
                        new GenericOnItemTouch.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                Intent it = new Intent(contextActivity, ExperimentoExpecifico.class);
                                it.putExtra("experimento", experimentos.get(position));
                                startActivity(it);
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onItemLongClick(View view, int position) {
                                Toast.makeText(contextActivity, "Visualizar informações", Toast.LENGTH_SHORT).show();
                            }
                        })
        );
        progressBarAndamento.setVisibility(View.GONE);
        ExperimentoViewModel.experimentos.setValue(experimentos);
    }

    private void inicializar() {
        experimentosRecycle = findViewById(R.id.experimentosRecycle);
        progressBarAndamento = findViewById(R.id.progressBarAndamento);
        contextActivity = this;

        experimentos = new ArrayList<>();

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.exp_andamento);
    }
}
