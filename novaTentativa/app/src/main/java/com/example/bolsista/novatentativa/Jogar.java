package com.example.bolsista.novatentativa;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bolsista.novatentativa.arquitetura.ClienteActivity;
import com.example.bolsista.novatentativa.arquitetura.Servidor;
import com.example.bolsista.novatentativa.configuracao.ConfigurarTeste;

import java.io.IOException;

public class Jogar extends AppCompatActivity {
    Button imagemButton;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //tirar barra de título
        //getActionBar().hide();
        setContentView(R.layout.activity_jogar);

        inicializar();
        escutar();

        if(Servidor.serverIdentificado){
            GerenciadorDeClientes.definirTela(this);
            int imagemAtual = Servidor.numberAleatorio;//pegar a imagem atual que está no servidor

            imagemButton.setBackgroundResource(imagemAtual);

        }else{
            Cliente.definirTela(this);
        }

        modoFullScreean();
    }

    private void escutar(){

            imagemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!Servidor.serverIdentificado) { //Se não for o servidor
                        ClienteActivity.enviar();
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

    public void informarDesconexao(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Jogar.this,"Um cliente foi desconectado",Toast.LENGTH_LONG).show();
                if(GerenciadorDeClientes.clientes.size()<2){
                    voltarTela();
                }
            }
        });
    }

    private void voltarTela(){

        Intent returnTelaServer = new Intent();
        Bundle b = new Bundle();
        b.putString("desconexao", "insuficiente");
        returnTelaServer.putExtras(b);
        setResult(Activity.RESULT_OK,returnTelaServer);
        finish();
    }

    private void modoFullScreean(){
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
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
