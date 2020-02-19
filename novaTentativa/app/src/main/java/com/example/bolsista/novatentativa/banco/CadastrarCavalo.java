package com.example.bolsista.novatentativa.banco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Cavalo;
import com.example.bolsista.novatentativa.viewsModels.ListarViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CadastrarCavalo extends AppCompatActivity {

    Button cadastrarCavaloBtn,cancelarBtn;
    TextView nome, idade, raca, detalhes;
    Context contextActivity;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    Cavalo cavalo;

    FirebaseAuth usuario;

    DocumentReference usuarioRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_cavalo);

        inicializar();
        listener();

        usuario = FirebaseAuth.getInstance();
        usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());
    }

    private void listener() {
        cadastrarCavaloBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cavalo = new Cavalo(nome.getText().toString(),raca.getText().toString(),
                        Integer.parseInt(idade.getText().toString()),detalhes.getText().toString(),
                        "", usuarioRef);

                addFireStore();

                nome.setText("");
                raca.setText("");
                idade.setText("");
                detalhes.setText("");
                Toast.makeText(getApplicationContext(),"Cavalo cadastrado", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void addFireStore(){
        db.collection("equinos")
                .add(cavalo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentReference.update("id",documentReference.getId());//adiciona ao campo id o id gerado pelo firebase
                        cavalo.setId(documentReference.getId());
                        ListarViewModel.addCavalo(cavalo);
                        Toast.makeText(contextActivity, "Equino adicionado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DataBase-FireStore-add", "Error adding document", e);
                    }
                });
    }

    public void getFireStore(){
        db.collection("equinos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("DataBase-FireStore-get", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.i("DataBase-FireStore-get", "Error getting documents.", task.getException());
                        }
                    }
                });

    }


    private void inicializar() {
        cadastrarCavaloBtn = findViewById(R.id.cadastrarCavaloBtn);
        cancelarBtn = findViewById(R.id.cancelarBtn);
        nome = findViewById(R.id.nomeTextView);
        idade = findViewById(R.id.idadeTextView);
        raca = findViewById(R.id.racaTextView);
        detalhes = findViewById(R.id.detalhesTextView);

        contextActivity = this;
    }
}
