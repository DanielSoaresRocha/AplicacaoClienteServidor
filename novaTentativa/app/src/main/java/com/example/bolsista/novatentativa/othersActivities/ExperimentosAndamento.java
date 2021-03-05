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
import com.example.bolsista.novatentativa.modelo.Equino;
import com.example.bolsista.novatentativa.modelo.Teste;
import com.example.bolsista.novatentativa.modelo.Experimento;
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
                                experimentos.add(experimento);
                            }
                            implementsRecycle();
                        } else {
                            Log.i("DataBase-FireStore-get", "Error getting documents.", task.getException());
                        }
                    }
                });
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
