package com.example.bolsista.novatentativa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastrarCavalo extends AppCompatActivity {

    Button cadastrarCavaloBtn;
    TextView nome, idade, raca, detalhes;


    //private FirebaseDatabase mFirebaseDatabase;
    //private DatabaseReference mDatabaseReference;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Map<String, Object> user = new HashMap<>();
    Map<String, Object> equino = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_cavalo);

        inicializar();

        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mDatabaseReference = mFirebaseDatabase.getReference().child("equinos-qi");

        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        //addFireStore();
        listener();


    }

    private void listener() {
        cadastrarCavaloBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                equino.put("idade",Integer.parseInt(idade.getText().toString()));
                equino.put("nome", nome.getText().toString());
                equino.put("raca",raca.getText().toString());
                equino.put("detalhes",detalhes.getText().toString());

                addFireStore();
                /*
                mDatabaseReference.push().setValue(new Cavalo(nome.getText().toString(),
                        raca.getText().toString(), Integer.parseInt(idade.getText().toString()),
                        detalhes.getText().toString()));*/
                nome.setText("");
                raca.setText("");
                idade.setText("");
                detalhes.setText("");
                Toast.makeText(getApplicationContext(),"Cavalo cadastrado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addFireStore(){
        db.collection("equinos")
                .add(equino)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("DataBase-FireStore", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DataBase-FireStore", "Error adding document", e);
                    }
                });
    }


    private void inicializar() {
        cadastrarCavaloBtn = findViewById(R.id.cadastrarCavaloBtn);
        nome = findViewById(R.id.nomeTextView);
        idade = findViewById(R.id.idadeTextView);
        raca = findViewById(R.id.racaTextView);
        detalhes = findViewById(R.id.detalhesTextView);
    }
}
