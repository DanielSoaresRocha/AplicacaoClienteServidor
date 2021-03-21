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
import android.view.LayoutInflater;
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
import com.example.bolsista.novatentativa.arquitetura.Servidor;
import com.example.bolsista.novatentativa.graficos.Estatistica;
import com.example.bolsista.novatentativa.graficos.GraficoLinha;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.example.bolsista.novatentativa.modelo.Teste;
import com.example.bolsista.novatentativa.modelo.Usuario;
import com.example.bolsista.novatentativa.recycleOnTouchLinesters.GenericOnItemTouch;
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

import me.drakeet.materialdialog.MaterialDialog;

public class Sessoes extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Button IniciarSessaoBtn, graficoLinhaBtn;
    private TextView experimentadorTextView, media, mediana;
    private RecyclerView sessaoRecycleView;
    private Spinner experimentadorSpinner;
    LinearLayout nenhumeSessaoLayouth;
    LinearLayout verGraficos;

    private ArrayList<Sessao> sessoes;
    ArrayList<Usuario> usuarios;
    private SessaoAdapter adapter;
    Context contextActivity;
    private Usuario usuarioSelecionado;

    Teste teste;

    //FireBase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usuarioRef;
    //Fire Base Auth
    FirebaseAuth usuario;

    private int aleatoriedade;

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

        graficoLinhaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Sessoes.this, GraficoLinha.class);
                // passando uma lista
                it.putExtra("sessoes", sessoes);
                it.putExtra("nomeEquino", ExperimentoViewModel.experimento.getValue()
                        .getEquino().getNome());
                startActivity(it);
            }
        });

        IniciarSessaoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TesteViewModel.iniciarTeste(teste, sessoes.size(), usuarioSelecionado);

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

        sessaoRecycleView.addOnItemTouchListener(
                new GenericOnItemTouch(
                        contextActivity,
                        sessaoRecycleView,
                        new GenericOnItemTouch.OnItemClickListener(){
                            @Override
                            public void onItemClick(View view, int position) {
                                if(aleatoriedade == 0){
                                    Toast.makeText(contextActivity, "Não é possível fazer isso com o Pré-teste",
                                            Toast.LENGTH_LONG).show();
                                }else {
                                    Intent it = new Intent(contextActivity, SessaoExpecifica.class);
                                    it.putExtra("sessao", sessoes.get(position));
                                    startActivity(it);
                                }
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        })
        );
    }
    @SuppressLint("SetTextI18n")
    private void preencher() {
        teste = (Teste) getIntent().getSerializableExtra("teste");
        aleatoriedade = getIntent().getIntExtra("aleatoriedade", 0);
        sessoes = teste.getSessoes();

        //Se não tiver nenhuma sessao
        if(sessoes.size() == 0){
            sessaoRecycleView.setVisibility(View.GONE);
            nenhumeSessaoLayouth.setVisibility(View.VISIBLE);
            verGraficos.setVisibility(View.GONE);
        }else{
            // média e mediana
            Estatistica estatistica = new Estatistica(sessoes);
            media.setText(estatistica.getMedia());
            mediana.setText(estatistica.getMediana());
        }

        // se o teste já tiver sido completado remover estas Views
        if(teste.isCompleto()){
            experimentadorTextView.setVisibility(View.GONE);
            experimentadorSpinner.setVisibility(View.GONE);
            IniciarSessaoBtn.setVisibility(View.GONE);
        }
        // mudar Titulo da barra
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(teste.getNome());
    }

    private void inicializar() {
        IniciarSessaoBtn = findViewById(R.id.IniciarSessaoBtn);
        experimentadorTextView = findViewById(R.id.experimentadorTextView);
        verGraficos = findViewById(R.id.verGraficos);
        sessaoRecycleView = findViewById(R.id.sessaoRecycleView);
        experimentadorSpinner = findViewById(R.id.experimentadorSpinner);
        nenhumeSessaoLayouth = findViewById(R.id.nenhumeSessaoLayouth);
        graficoLinhaBtn = findViewById(R.id.graficoLinhaBtn);
        media = findViewById(R.id.media);
        mediana = findViewById(R.id.mediana);
        contextActivity = this;
        sessoes = new ArrayList<>();
        usuarios = new ArrayList<>();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String nomeUsuario = parent.getItemAtPosition(position).toString();

        for(int i = 0; i < usuarios.size(); i++){
            if(usuarios.get(i).getNome().equals(nomeUsuario)){
                usuarioSelecionado = usuarios.get(i);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
