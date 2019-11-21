package com.example.bolsista.novatentativa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastrarCavalo extends AppCompatActivity {

    Button cadastrarCavaloBtn;
    TextView nome, idade, raca, detalhes;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_cavalo);

        inicializar();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("equinos-qi");

        listener();


    }

    private void listener() {
        cadastrarCavaloBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.push().setValue(new Cavalo(nome.getText().toString(),
                        raca.getText().toString(), Integer.parseInt(idade.getText().toString()),
                        detalhes.getText().toString()));

                nome.setText("");
                raca.setText("");
                idade.setText("");
                detalhes.setText("");
                Toast.makeText(getApplicationContext(),"Cavalo cadastrado", Toast.LENGTH_SHORT).show();
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
