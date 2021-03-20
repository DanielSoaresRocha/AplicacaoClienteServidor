package com.example.bolsista.novatentativa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bolsista.novatentativa.arquitetura.Servidor;
import com.example.bolsista.novatentativa.cadastros.CadastrarEquino;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.viewsModels.ExperimentoViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Resultado extends AppCompatActivity {
    private Context contextoAtivity;
    private Button voltar;

    //FireBase FireStore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usuarioRef;
    //FireBase autenth
    FirebaseAuth usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        inicializar();
        listener();
        if(Servidor.serverAtivo)
            updateExperimento();
    }

    private void listener() {
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaInicial = new Intent(Resultado.this, Inicio.class);
                startActivity(telaInicial);
            }
        });
    }

    private void updateExperimento() {
        db.collection("experimentos")
                .document(ExperimentoViewModel.experimento.getValue().getId())
                .set(ExperimentoViewModel.experimento.getValue())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FireStore", "DocumentSnapshot successfully updated!");
                        Toast.makeText(contextoAtivity, "Experimento Atualizado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FireStore", "Error updating document", e);
                    }
                });
    }

    private void inicializar() {
        contextoAtivity = this;

        usuario = FirebaseAuth.getInstance();
        usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());
        voltar = findViewById(R.id.voltarButton);
    }
}
