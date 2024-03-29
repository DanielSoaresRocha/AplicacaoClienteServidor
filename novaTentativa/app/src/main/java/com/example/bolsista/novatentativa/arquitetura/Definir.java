package com.example.bolsista.novatentativa.arquitetura;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.bolsista.novatentativa.NovoExperimento;
import com.example.bolsista.novatentativa.R;


public class Definir extends AppCompatActivity {
    private ImageView controle,mestre,escravo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definir);

        inicializar();
        listener();
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
                Intent telaServidor = new Intent(Definir.this, Servidor.class);
                startActivity(telaServidor);
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
