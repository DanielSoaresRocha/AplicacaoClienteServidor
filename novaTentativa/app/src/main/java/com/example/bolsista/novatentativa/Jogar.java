package com.example.bolsista.novatentativa;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.bolsista.novatentativa.configuracao.ConfigurarTeste;

import java.io.IOException;

public class Jogar extends AppCompatActivity {
    Button imagemButton;
    MediaPlayer mp;

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

    private void escutar(){

            imagemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!MainActivity.serverIdentificado) { //Se não for o servidor
                        MainActivity.enviar2();

                    }else {
                        tocarError();
                    }
                }
            });

        }

    public void tocarError(){
        mp = MediaPlayer.create(Jogar.this, ConfigurarTeste.configuracao.getSomErro());
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();
                mp = null;
            }
        });
        mp.start();
    }

    public void tocarAcerto(){
        mp = MediaPlayer.create(Jogar.this, ConfigurarTeste.configuracao.getSomAcerto());
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();
                mp = null;
            }
        });
        mp.start();
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
