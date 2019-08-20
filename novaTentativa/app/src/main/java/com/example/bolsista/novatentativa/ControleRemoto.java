package com.example.bolsista.novatentativa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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


    }

    private void iniciar() {
        controleRemoto = findViewById(R.id.controleRemoto);
        desconect = findViewById(R.id.desconect);
    }
}
