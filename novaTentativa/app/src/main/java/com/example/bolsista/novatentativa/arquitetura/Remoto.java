package com.example.bolsista.novatentativa.arquitetura;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bolsista.novatentativa.sockets.Cliente;
import com.example.bolsista.novatentativa.ControleRemoto;
import com.example.bolsista.novatentativa.R;

public class Remoto extends AppCompatActivity {
    public EditText remotoEditText;
    public Button criarRemotoBtn, comecarRemotoBtn;

    public static Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remoto);

        inicializar();
        comecar();
        listener();

    }

    private void comecar() {
        comecarRemotoBtn.setVisibility(View.GONE);
    }

    private void listener() {
        criarRemotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarControleRemoto();
            }
        });

        comecarRemotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent controleRemoto = new Intent(Remoto.this, ControleRemoto.class);
                startActivity(controleRemoto);
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
        }
    }
    private void inicializar() {
        remotoEditText = findViewById(R.id.remotoEditText);
        criarRemotoBtn = findViewById(R.id.criarRemotoBtn);
        comecarRemotoBtn = findViewById(R.id.comecarRemotoBtn);
    }
}
