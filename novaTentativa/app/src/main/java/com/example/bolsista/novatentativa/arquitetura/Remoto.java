package com.example.bolsista.novatentativa.arquitetura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bolsista.novatentativa.Cliente;
import com.example.bolsista.novatentativa.ControleRemoto;
import com.example.bolsista.novatentativa.R;

public class Remoto extends AppCompatActivity {
    EditText remotoEditText;
    Button criarRemotoBtn;

    public static Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remoto);

        inicializar();
        listener();

    }

    private void listener() {
        criarRemotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarControleRemoto();
            }
        });
    }

    public void criarControleRemoto(){
        if(remotoEditText.getText().toString().length() < 5){
            Toast.makeText(getApplicationContext(),R.string.ip_invalido, Toast.LENGTH_LONG).show();
        }else{
            final Remoto remoto = this;

            cliente = new Cliente(remotoEditText.getText().toString(),remoto,true);
            cliente.connect();
            Toast.makeText(getApplicationContext(), R.string.controle_ativo, Toast.LENGTH_SHORT).show();

            Intent controleRemoto = new Intent(Remoto.this, ControleRemoto.class);
            startActivity(controleRemoto);

        }
    }
    private void inicializar() {
        remotoEditText = findViewById(R.id.remotoEditText);
        criarRemotoBtn = findViewById(R.id.criarRemotoBtn);
    }
}