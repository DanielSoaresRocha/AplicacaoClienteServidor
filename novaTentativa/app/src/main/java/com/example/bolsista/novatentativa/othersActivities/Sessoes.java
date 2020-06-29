package com.example.bolsista.novatentativa.othersActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.SessaoAdapter;
import com.example.bolsista.novatentativa.arquitetura.Definir;
import com.example.bolsista.novatentativa.arquitetura.Servidor;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.example.bolsista.novatentativa.modelo.Teste;
import com.example.bolsista.novatentativa.modelo.Usuario;
import com.example.bolsista.novatentativa.viewsModels.ExperimentoViewModel;
import com.example.bolsista.novatentativa.viewsModels.TesteViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Sessoes extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Button IniciarSessaoBtn;
    private TextView experimentadorTextView;
    private RecyclerView sessaoRecycleView;
    private Spinner experimentadorSpinner;
    LinearLayout nenhumeSessaoLayouth;
    Button verGrafico;

    private int POSITION_EXPERIMENTO;
    private int POSITION_TESTE;
    private ArrayList<Sessao> sessoes;
    ArrayList<Usuario> usuarios;
    private SessaoAdapter adapter;
    Context contextActivity;

    //FireBase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usuarioRef;
    //Fire Base Auth
    FirebaseAuth usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessoes);

        inicializar();
        preencher();
        implementsRecycle();
        listener();
        pegarUsuariosFireStore();

        usuario = FirebaseAuth.getInstance();
        usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());
    }

    private void pegarUsuariosFireStore() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Usuario usuario = document.toObject(Usuario.class);
                                usuarios.add(usuario);
                            }
                            trocarPosicao();
                            spinner();
                        } else {
                            Log.i("DataBase-FireStore-get", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    //colocar o usuario logado como primeiro no ArrayList
    private void trocarPosicao(){
        for(int i = 0; i < usuarios.size(); i++){
            if(usuarios.get(i).getNome().equals(usuario.getCurrentUser().getDisplayName())){
                Collections.swap(usuarios, i, 0); //troca as posições
            }
        }
    }

    private void spinner() {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, usuarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        experimentadorSpinner.setAdapter(adapter);
    }

    private void listener() {
        experimentadorSpinner.setOnItemSelectedListener(this);

        verGrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contextActivity, "Ainda não implementado", Toast.LENGTH_SHORT).show();
            }
        });

        IniciarSessaoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Teste teste = ExperimentoViewModel.experimentos.getValue().get(POSITION_EXPERIMENTO)
                        .getTestes().get(POSITION_TESTE);
                TesteViewModel.iniciarTeste(teste);

                Intent telaServidor = new Intent(Sessoes.this, Servidor.class);
                startActivity(telaServidor);
            }
        });
    }

    private void implementsRecycle() {
        adapter = new SessaoAdapter(contextActivity, sessoes);
        sessaoRecycleView.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextActivity, LinearLayoutManager.VERTICAL, false);
        sessaoRecycleView.setLayoutManager(layout);
    }
    private void preencher() {
        Intent it = getIntent();
        POSITION_EXPERIMENTO = it.getIntExtra("positionExperimento", 0);
        POSITION_TESTE = it.getIntExtra("positionTeste", 0);

        sessoes = ExperimentoViewModel.experimentos.getValue().get(POSITION_EXPERIMENTO)
                .getTestes().get(POSITION_TESTE).getSessoes();
        //Se não tiver nenhuma sessao
        if(sessoes.size() == 0){
            sessaoRecycleView.setVisibility(View.GONE);
            nenhumeSessaoLayouth.setVisibility(View.VISIBLE);
            verGrafico.setVisibility(View.GONE);
        }

        // se o teste já tiver sido completado remover estas Views
        if(it.getBooleanExtra("completo",false)){
            experimentadorTextView.setVisibility(View.GONE);
            experimentadorSpinner.setVisibility(View.GONE);
            IniciarSessaoBtn.setVisibility(View.GONE);
        }
        // mudar Titulo da barra
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(it.getStringExtra("nomeTeste"));
    }

    private void inicializar() {
        IniciarSessaoBtn = findViewById(R.id.IniciarSessaoBtn);
        experimentadorTextView = findViewById(R.id.experimentadorTextView);
        verGrafico = findViewById(R.id.verGrafico);
        sessaoRecycleView = findViewById(R.id.sessaoRecycleView);
        experimentadorSpinner = findViewById(R.id.experimentadorSpinner);
        nenhumeSessaoLayouth = findViewById(R.id.nenhumeSessaoLayouth);
        contextActivity = this;
        sessoes = new ArrayList<>();
        usuarios = new ArrayList<>();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
