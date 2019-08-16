package com.example.bolsista.novatentativa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Jogar extends AppCompatActivity {
    Button imagemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogar);


        if(MainActivity.serverIdentificado){
            GerenciadorDeClientes.definirTela(this);
        }else{
            Cliente.definirTela(this);
        }


        inicializar();
        escutar();


    }

    private void escutar() {

        imagemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.enviar2();
            }
        });

    }

    private void inicializar() {
        imagemButton = findViewById(R.id.imagemButton);
    }


    public Button getImagemButton() {
        return imagemButton;
    }

    public void setImagemButton(Button imagemButton) {
        this.imagemButton = imagemButton;
    }
}
