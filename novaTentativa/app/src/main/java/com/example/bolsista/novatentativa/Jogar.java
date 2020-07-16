package com.example.bolsista.novatentativa;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bolsista.novatentativa.arquitetura.ClienteActivity;
import com.example.bolsista.novatentativa.arquitetura.Servidor;
import com.example.bolsista.novatentativa.modelo.Desafio;
import com.example.bolsista.novatentativa.sockets.Cliente;
import com.example.bolsista.novatentativa.sockets.PreTeste;
import com.example.bolsista.novatentativa.sockets.PseudoTeste;
import com.example.bolsista.novatentativa.viewsModels.TesteViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class Jogar extends AppCompatActivity {
    public Button imagemButton;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //tirar barra de título
        //getActionBar().hide();
        setContentView(R.layout.activity_jogar);
        //Colocar tela em landScape para melhor visualização
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);

        inicializar();
        listener();

        if(Servidor.preTeste){
            Toast.makeText(this, "definiu", Toast.LENGTH_SHORT).show();
            PreTeste.definirTela(this);
            int imagemAtual = Servidor.numberAleatorio; //pegar a imagem atual que está no servidor
            imagemButton.setBackgroundResource(imagemAtual);
            if(!TesteViewModel.teste.getValue().getPreTeste()) // só se não for um pré-teste
                PseudoTeste.comecarInteracao();
        }else{
            Cliente.definirTela(this);
        }
    }

    private void listener(){
            imagemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!Servidor.preTeste) { //Se não for o servidor
                        ClienteActivity.enviar();
                    }else {
                        tocarError();
                    }
                }
            });

        }

    public void tocarError(){
        mp = MediaPlayer.create(Jogar.this, TesteViewModel.teste.getValue().getSomErro());
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
        mp = MediaPlayer.create(Jogar.this, TesteViewModel.teste.getValue().getSomAcerto());
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();
                mp = null;
            }
        });
        mp.start();
    }

    public void informarDesconexao(int numberClientes){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Jogar.this,"Um cliente foi desconectado",Toast.LENGTH_LONG).show();
                if(numberClientes < 2){
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

    // Terminar o teste recebendo todos os desafios realizados para enviar ao banco
    public void terminar(){
        Intent telaResultado = new Intent(Jogar.this, Resultado.class);
        startActivity(telaResultado);

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
