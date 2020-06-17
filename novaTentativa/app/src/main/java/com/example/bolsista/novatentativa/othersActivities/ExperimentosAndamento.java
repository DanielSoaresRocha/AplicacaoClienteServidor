package com.example.bolsista.novatentativa.othersActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.IniciarConfiguracao;
import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.ExperimentoAdapter;
import com.example.bolsista.novatentativa.fragments.CadastrarExperimento;
import com.example.bolsista.novatentativa.modelo.Configuracao;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.modelo.Experimento2;
import com.example.bolsista.novatentativa.recycleOnTouchLinesters.ListarCavalosOnItemTouch;
import com.example.bolsista.novatentativa.viewsModels.ListarViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

import me.drakeet.materialdialog.MaterialDialog;

public class ExperimentosAndamento extends AppCompatActivity {
    private RecyclerView experimentosRecycle;

    Context contextActivity;
    private ExperimentoAdapter adapter;

    ArrayList<Experimento2> experimentos2 = new ArrayList<>();

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
        preencher();
        implementsRecycle();
    }

    private void preencher() {
        ArrayList<Integer> imagens = new ArrayList<Integer>() {
            {
                add(1);
                add(2);
                add(3);
            }
        };

        Configuracao c1 = new Configuracao("id", "Pré-teste", "Teste gabor",
                imagens, 10, 5, 5, 234, 435,
                null);
        Configuracao c2 = new Configuracao("id2", "Pré-teste2", "Teste gabor2",
                imagens, 10, 5, 5, 234, 435,
                null);

        ArrayList<Configuracao> testes = new ArrayList<>();
        testes.add(c1);
        testes.add(c2);

        experimentos2.add(new Experimento2("id1", "cavalo 1", "experimento 1", new Date(),
                new Date(), testes));
        experimentos2.add(new Experimento2("id2", "cavalo 2", "experimento 2", new Date(),
                new Date(), testes));
        experimentos2.add(new Experimento2("id3", "cavalo 3", "experimento 3", new Date(),
                new Date(), testes));
    }

    private void implementsRecycle(){
        adapter = new ExperimentoAdapter(contextActivity, experimentos2);
        experimentosRecycle.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextActivity,LinearLayoutManager.VERTICAL,false);
        experimentosRecycle.setLayoutManager(layout);
    }

    private void inicializar() {
        experimentosRecycle = findViewById(R.id.experimentosRecycle);
        contextActivity = this;

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.exp_andamento);

        experimentosRecycle.addOnItemTouchListener(
                new ListarCavalosOnItemTouch(
                        contextActivity,
                        experimentosRecycle,
                        new ListarCavalosOnItemTouch.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                Toast.makeText(contextActivity, "vai para outra tela", Toast.LENGTH_SHORT).show();
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onItemLongClick(View view, int position) {
                                Toast.makeText(contextActivity, "Visualizar informações", Toast.LENGTH_SHORT).show();
                            }
                        })
        );

    }
}
