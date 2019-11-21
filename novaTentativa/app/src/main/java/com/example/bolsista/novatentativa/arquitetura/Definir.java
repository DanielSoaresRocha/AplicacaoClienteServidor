package com.example.bolsista.novatentativa.arquitetura;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.bolsista.novatentativa.Cavalo;
import com.example.bolsista.novatentativa.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Definir extends AppCompatActivity {

    ImageView controle,mestre,escravo;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definir);

        inicializar();
        listener();
        conectarAoBanco();

    }

    private void conectarAoBanco() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("equinos-qi");

        mDatabaseReference.push().setValue(new Cavalo("Robert","napolitanakkk",5));
    }

    private void listener() {

        controle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaControle = new Intent(Definir.this, Remoto.class);
                startActivity(telaControle);
            }
        });

        mestre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaMestre = new Intent(Definir.this, Servidor.class);
                startActivity(telaMestre);
            }
        });

        escravo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaEscravo = new Intent(Definir.this, ClienteActivity.class);
                startActivity(telaEscravo);
            }
        });
    }


    private void inicializar() {
        controle = findViewById(R.id.controleImgView);
        mestre = findViewById(R.id.mestreImgView);
        escravo = findViewById(R.id.escravoImgView);
    }
}
