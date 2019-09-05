package com.example.bolsista.novatentativa;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Cliente {

    private ObjectInputStream leitor;//****
    private ObjectOutputStream escritor;//*****

    private String host;


    private Socket cliente;
    private MainActivity client;
    static Jogar jogar;

    private String imgAtual;

    private Boolean controleRemoto;

    public Cliente(String host, MainActivity client, boolean controleRemoto){
        this.host = host;
        this.client = client;
        this.controleRemoto = controleRemoto;
    }


    public void connect(){
        imgAtual = "1"; //////////////////DESTAQUE

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
        cliente = new Socket(host,9999);



        //lendo mensagens do servidor
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    leitor = new ObjectInputStream(cliente.getInputStream());
                    Log.i("OBJETO","Criou input do CLIENTE");

                    while (true){
                        String mensagem = leitor.readUTF(); ///******
                        Log.i("COMUNICACAO","MENSAGEM RECEBIDA DO SERVER ="+ mensagem);

                        mudarImagem(mensagem);
                    }

                }catch (IOException e){
                    Log.i("ERRO","IMPOSSÍVEL LER MENSAGEM");
                }
            }
        }).start();

            //criando escrita

            escritor = new ObjectOutputStream(cliente.getOutputStream());
            Log.i("OBJETO","Criou output do CLIENTE");


            if(!controleRemoto){
                escritor.writeUTF("CLIENTE PADRÃO");
                escritor.flush();
            }else{
                escritor.writeUTF("remoto");
                escritor.flush();
            }

            ativarBotao();
        }catch (IOException e){
            Log.i("ERRO","ERRO AO CONECTAR-SE AO SERVIDOR "+ e.getMessage());
        }
    }



    //escrevendo para o servidor - cliente Padrão
    public void escrever(){
        try{
            Log.i("COMUNICACAO","MENSAGEM ENVIADA AO SERVIDOR:  "+ imgAtual);
            escritor.writeUTF(imgAtual);
            escritor.flush();

        }catch (IOException e){
            Log.i("ERRO", "erro ao enviar mensagem ao servidor" + e.getMessage());
        }
    }

    public void controleRemoto(){
        try{
            Log.i("COMUNICACAO", "CONTROLE REMOTO ACIONADO");
            escritor.writeUTF("mudar");
            escritor.flush();

        }catch (IOException e){
            Log.i("ERRO", "erro ao enviar no controle remoto" + e.getMessage());
        }

    }

    public void desconect(){

        try{
            escritor.writeUTF("desconect");

            escritor.close();
            leitor.close();

            cliente.close();

            Log.i("REMOTO","CONEXÃO COM SERVIDOR FECHADA");
        }catch (IOException e){
            Log.i("ERRO", "ERRO AO FECHAR CLIENTE = " + e.getMessage());
        }

    }


    private void mudarImagem(String msg) {
        final String comando = msg;

            jogar.imagemButton.post(new Runnable() {
                @Override
                public void run() {
                    if(comando.equals("branco")){
                        jogar.getImagemButton().setBackgroundResource(R.drawable.branco);
                    }else if(comando.equals("1")){
                        jogar.getImagemButton().setBackgroundResource(R.drawable.circulo);
                    }else if(comando.equals("2")){
                        jogar.getImagemButton().setBackgroundResource(R.drawable.coracao);
                    }else if(comando.equals("3")){
                        jogar.getImagemButton().setBackgroundResource(R.drawable.losango);
                    }else if(comando.equals("4")){
                        jogar.getImagemButton().setBackgroundResource(R.drawable.triangulo);
                    }else if(comando.equals("5")){
                        jogar.getImagemButton().setBackgroundResource(R.drawable.estrela2);
                    }else if(comando.equals("6")){
                        jogar.getImagemButton().setBackgroundResource(R.drawable.hexagono);
                    }else{
                        jogar.getImagemButton().setBackgroundResource(R.drawable.retangulo);
                    }
                }
            });

            imgAtual = msg;

    }

    private void ativarBotao() {
        client.comecar.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(client.getApplicationContext(),R.string.comecar,Toast.LENGTH_LONG).show();
                client.comecar.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void definirTela(Jogar jogarr){
        jogar = jogarr;

    }

}
