package com.example.bolsista.novatentativa.othersActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Configuracao;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.modelo.Experimento2;
import com.example.bolsista.novatentativa.viewsModels.ListarViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class ExperimentosAndamento extends AppCompatActivity {


    Context contextActivity;

    ArrayList<Experimento2> experimento2 = new ArrayList<>();

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

        experimento2.add(new Experimento2("id1", "cavalo 1", "experimento 1", new Date(),
                new Date(), testes));
        experimento2.add(new Experimento2("id2", "cavalo 2", "experimento 2", new Date(),
                new Date(), testes));
        experimento2.add(new Experimento2("id3", "cavalo 3", "experimento 3", new Date(),
                new Date(), testes));
    }

    private void inicializar() {
        contextActivity = this;

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.exp_andamento);

    }
}
