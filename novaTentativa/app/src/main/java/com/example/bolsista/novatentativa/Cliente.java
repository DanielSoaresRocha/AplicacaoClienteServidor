package com.example.bolsista.novatentativa;

import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Cliente {

    private String host;

    private PrintWriter escritor;

    private Socket cliente;
    private MainActivity client;
    static Jogar jogar;

    private String imgAtual;

    public Cliente(String host, MainActivity client){
        this.host = host;
        this.client = client;
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
                    BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));


                    while (true){
                        String mensagem = leitor.readLine();

                        Log.i("COMUNICACAO","MENSAGEM RECEBIDA DO SERVER ="+ mensagem);

                        mudarImagem(mensagem);
                    }

                }catch (IOException e){
                    Log.i("ERRO","IMPOSSÍVEL LER MENSAGEM");
                }
            }
        }).start();

            //criando escrita
            escritor = new PrintWriter(cliente.getOutputStream(),true);
            escritor.println("CONECTEI");

        }catch (IOException e){
            Log.i("ERRO","ERRO AO CONECTAR-SE AO SERVIDOR "+ e.getMessage());
        }
    }

    //escrevendo para o servidor
    public void escrever(){
        Log.i("COMUNICACAO","MENSAGEM ENVIADA AO SERVIDOR:  "+ imgAtual);
        escritor.println(imgAtual);

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

    public static void definirTela(Jogar jogarr){
        jogar = jogarr;

    }

}
