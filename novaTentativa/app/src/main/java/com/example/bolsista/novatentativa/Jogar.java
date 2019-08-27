package com.example.bolsista.novatentativa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class Jogar extends AppCompatActivity {
    Button imagemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //tirar barra de título
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

        if(!MainActivity.serverIdentificado){  //Se não for o servidor
            imagemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    MainActivity.enviar2();

                    }catch (IOException e){
                        Log.i("ERRO","ERRO AO ENVIAR MENSAGEM");
                    }
                }
            });
            }

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
