package com.example.bolsista.novatentativa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ControleRemoto extends AppCompatActivity {
    Button controleRemoto,desconect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle_remoto);

        iniciar();
        listener();

    }

    private void listener() {

        controleRemoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.cliente.controleRemoto(); //enviar comando
            }
        });

        desconect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.cliente.desconect();
                Toast.makeText(getApplicationContext(),R.string.controle_inativo,Toast.LENGTH_LONG).show();

                Intent controleRemoto = new Intent(ControleRemoto.this,MainActivity.class);
                startActivity(controleRemoto);
            }
        });

    }

    private void iniciar() {
        controleRemoto = findViewById(R.id.controleRemoto);
        desconect = findViewById(R.id.desconect);
    }
}
