package com.example.bolsista.novatentativa;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.arquitetura.Remoto;

public class ControleRemoto extends AppCompatActivity {
    Button desconect;
    ImageView controleRemoto;

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
                Remoto.cliente.controleRemoto(); //enviar comando
            }
        });

        desconect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Remoto.cliente.desconect();
                Toast.makeText(getApplicationContext(),R.string.controle_inativo,Toast.LENGTH_LONG).show();

                Intent controleRemoto = new Intent(ControleRemoto.this,Remoto.class);
                startActivity(controleRemoto);

                finish();
            }
        });

    }



    private void iniciar() {
        controleRemoto = findViewById(R.id.controleRemoto);
        desconect = findViewById(R.id.desconect);
    }
}
