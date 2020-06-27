package com.example.bolsista.novatentativa;

import android.app.Activity;
import android.content.Intent;
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

    public static ArrayList<Desafio> desafios;

    //FireBase FireStore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usuarioRef;
    //FireBase autenth
    FirebaseAuth usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //tirar barra de título
        //getActionBar().hide();
        setContentView(R.layout.activity_jogar);

        inicializar();
        listener();

        if(Servidor.preTeste){
            Toast.makeText(this, "definiu", Toast.LENGTH_SHORT).show();
            PreTeste.definirTela(this);
            int imagemAtual = Servidor.numberAleatorio;//pegar a imagem atual que está no servidor

            imagemButton.setBackgroundResource(imagemAtual);
        }else{
            Cliente.definirTela(this);
        }

        /*if(Servidor.serverIdentificado){
            GerenciadorDeClientes.definirTela(this);
            int imagemAtual = Servidor.numberAleatorio;//pegar a imagem atual que está no servidor

            imagemButton.setBackgroundResource(imagemAtual);
        }else{
            Cliente.definirTela(this);
        }
        modoFullScreean();
        */
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

        mp = MediaPlayer.create(Jogar.this, NovoExperimento.testeSelecionada.getSomErro());
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
        System.out.println(NovoExperimento.testeSelecionada.getSomAcerto()+"---------------------------");
        mp = MediaPlayer.create(Jogar.this, NovoExperimento.testeSelecionada.getSomAcerto());
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
        telaResultado();
    }

    /*
    public void terminar(ArrayList<Desafio> desafios, String idExperimento){
        this.desafios = desafios;
        DocumentReference experimentoRef = db.collection("experimentos").document(idExperimento);
        for (int i = 0; i < desafios.size(); i++){
            desafios.get(i).setExperimento(experimentoRef);
            addDesafioFireStore(desafios.get(i));
        }
        telaResultado();
    }
        */

    // Tela para exibir o resultado
    private void telaResultado() {
        Intent telaResultado = new Intent(Jogar.this, Resultado.class);
        startActivity(telaResultado);
    }

    private void addDesafioFireStore(Desafio desafio){
        db.collection("desafios")
                .add(desafio)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentReference.update("id",documentReference.getId());//adiciona ao campo id o id gerado pelo firebase
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DataBase-FireStore-add", "Error adding document", e);
                    }
                });
    }


    private void modoFullScreean(){
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


    private void inicializar() {
        imagemButton = findViewById(R.id.imagemButton);

        usuario = FirebaseAuth.getInstance();
        try {
            usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());
        }catch (java.lang.NullPointerException e){
            usuarioRef = db.collection("users").document("zl1hFltVOlJONAVUeIsY");
        }
    }


    public Button getImagemButton() {
        return imagemButton;
    }

    public void setImagemButton(Button imagemButton) {
        this.imagemButton = imagemButton;
    }
}
