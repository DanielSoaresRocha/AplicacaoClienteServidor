package com.example.bolsista.novatentativa.arquitetura;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.bolsista.novatentativa.banco.CadastrarCavalo;
import com.example.bolsista.novatentativa.banco.ListarCavalos;
import com.example.bolsista.novatentativa.R;

public class Definir extends AppCompatActivity {

    ImageView controle,mestre,escravo;
    Button testeBtn, listarBtn;

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

        testeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaCadastro = new Intent(Definir.this, CadastrarCavalo.class);
                startActivity(telaCadastro);
            }
        });

        listarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaListar = new Intent(Definir.this, ListarCavalos.class);
                startActivity(telaListar);
            }
        });

    }


    private void inicializar() {
        controle = findViewById(R.id.controleImgView);
        mestre = findViewById(R.id.mestreImgView);
        escravo = findViewById(R.id.escravoImgView);
        testeBtn = findViewById(R.id.testeBtn);
        listarBtn = findViewById(R.id.listarBtn);
    }
}
