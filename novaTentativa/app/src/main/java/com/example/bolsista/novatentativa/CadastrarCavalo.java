package com.example.bolsista.novatentativa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CadastrarCavalo extends AppCompatActivity {

    Button cadastrarCavaloBtn,cancelarBtn;
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
                nome.setText("");
                raca.setText("");
                idade.setText("");
                detalhes.setText("");
                Toast.makeText(getApplicationContext(),"Cavalo cadastrado", Toast.LENGTH_SHORT).show();
            }
        });

        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFireStore();
            }
        });
    }

    public void addFireStore(){
        db.collection("equinos")
                .add(equino)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("DataBase-FireStore-add", "DocumentSnapshot added with ID: " + documentReference.getId());
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
    }
}
