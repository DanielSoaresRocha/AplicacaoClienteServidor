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
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.SessaoAdapter;
import com.example.bolsista.novatentativa.modelo.Experimento2;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.example.bolsista.novatentativa.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class Sessoes extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Button IniciarSessaoBtn;
    private TextView experimentadorTextView;
    private RecyclerView sessaoRecycleView;
    private Spinner experimentadorSpinner;
    Button verGrafico;

    private int POSITION_EXPERIMENTO;
    private int POSITION_TESTE;
    private ArrayList<Sessao> sessoes;
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
        preencherArray();
        pegarPosicao();
        implementsRecycle();
        listener();
        spinner();

        usuario = FirebaseAuth.getInstance();
        usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());
    }

    private void spinner() {
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios.add(new Usuario("id1", "Daniel"));
        usuarios.add(new Usuario("id1", "Mário"));
        usuarios.add(new Usuario("id1", "Leonardo"));
        usuarios.add(new Usuario("id1", "Marcos"));
        usuarios.add(new Usuario("id1", "Beatriz"));

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, usuarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        experimentadorSpinner.setAdapter(adapter);
    }

    private void listener() {
        experimentadorSpinner.setOnItemSelectedListener(this);

        verGrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFireBase();
            }
        });
    }

    private void addToFireBase(){
        /*
        Experimento2 experimento2 = ExperimentosAndamento.experimentos2.get(POSITION_EXPERIMENTO);
        experimento2.getTestes().get(POSITION_TESTE).setSessoes(sessoes);

        db.collection("configuracoes2")
                .add(experimento2)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentReference.update("id",documentReference.getId());//adiciona ao campo id o id gerado pelo firebase
                        experimento2.setId(documentReference.getId());
                        Toast.makeText(contextActivity, "Experimento adicionado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DataBase-FireStore-add", "Error adding document", e);
                    }
                });*/
        ArrayList<Experimento2> experimentos2 = new ArrayList<>();
        db.collection("configuracoes2")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Experimento2 experimento2 = document.toObject(Experimento2.class);
                                experimentos2.add(experimento2);
                            }

                            for(Experimento2 experimento2: experimentos2){
                                try {
                                    System.out.println("-------------------------------------");
                                    System.out.println(experimento2.getTestes().get(0).getSessoes()
                                            .get(0).getExperimentador().getNome());
                                }catch (java.lang.NullPointerException e){
                                    System.out.println("Esse nao deu");
                                }

                            }
                        } else {
                            Log.i("DataBase-FireStore-get", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void preencherArray() {
        sessoes.add(new Sessao("id1", "Sessao 1", new Date(), 25,
                new Usuario("id1", "Daniel")));
        sessoes.add(new Sessao("id2", "Sessao 2", new Date(), 40,
                new Usuario("id2", "Roberto")));
        sessoes.add(new Sessao("id3", "Sessao 3", new Date(), 65,
                new Usuario("id3", "Sara")));
        sessoes.add(new Sessao("id4", "Sessao 4", new Date(), 80,
                new Usuario("id4", "Cristina")));
        sessoes.add(new Sessao("id5", "Sessao 5", new Date(), 95,
                new Usuario("id6", "Lucas")));
    }

    private void implementsRecycle() {
        adapter = new SessaoAdapter(contextActivity, sessoes);
        sessaoRecycleView.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextActivity, LinearLayoutManager.VERTICAL, false);
        sessaoRecycleView.setLayoutManager(layout);
    }
    private void pegarPosicao() {
        Intent it = getIntent();
        POSITION_EXPERIMENTO = it.getIntExtra("positionExperimento", 0);
        POSITION_TESTE = it.getIntExtra("positionTeste", 0);
        POSITION_TESTE = it.getIntExtra("positionTeste", 0);

        // se o experimento tiver sido completado remover estas Views
        if(it.getBooleanExtra("completo",false)){
            experimentadorTextView.setVisibility(View.GONE);
            experimentadorSpinner.setVisibility(View.GONE);
            IniciarSessaoBtn.setVisibility(View.GONE);
        }
        // mudar Titulo da barra
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(it.getStringExtra("nomeTeste"));
        /*
        sessoes = ExperimentosAndamento.experimentos2.get(POSITION_EXPERIMENTO)
                .getTestes().get(POSITION_TESTE)
                .getSessoes();*/
    }

    private void inicializar() {
        IniciarSessaoBtn = findViewById(R.id.IniciarSessaoBtn);
        experimentadorTextView = findViewById(R.id.experimentadorTextView);
        verGrafico = findViewById(R.id.verGrafico);
        sessaoRecycleView = findViewById(R.id.sessaoRecycleView);
        experimentadorSpinner = findViewById(R.id.experimentadorSpinner);
        contextActivity = this;
        sessoes = new ArrayList<>();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
