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

    public Cliente(String host, MainActivity client){
        this.host = host;
        this.client = client;
    }


    public void connect(){

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
                        Log.i("COMUNICACAO","MENSAGEM RECEBIDA DO SERVER"+ mensagem);
                    }

                }catch (IOException e){
                    Log.i("ERRO","IMPOSSÍVEL LER MENSAGEM");
                }
            }
        }).start();

            //criando escrita
            escritor = new PrintWriter(cliente.getOutputStream(),true);

        }catch (IOException e){
            Log.i("ERRO","ERRO AO CONECTAR-SE AO SERVIDOR "+ e.getMessage());
        }
    }

    //escrevendo para o servidor
    public void escrever(String msg){
        Log.i("COMUNICACAO","MENSAGEM ENVIADA AO SERVIDOR:  "+ msg);
        escritor.println(msg);
        mudarImagem();

    }

    private void mudarImagem() {
            client.imagem.post(new Runnable() {
                @Override
                public void run() {
                    Random radom  = new Random(); // gerar número aleatório
                    int numeroTmp = radom.nextInt(6);

                    if(numeroTmp == 0){
                        client.imagem.setBackgroundResource(R.drawable.circulo);
                    }else if(numeroTmp == 1){
                        client.imagem.setBackgroundResource(R.drawable.estrela);
                    }else if(numeroTmp == 2){
                        client.imagem.setBackgroundResource(R.drawable.hexagono);
                    }else if(numeroTmp == 3){
                        client.imagem.setBackgroundResource(R.drawable.retangulo);
                    }else if(numeroTmp == 4){
                        client.imagem.setBackgroundResource(R.drawable.triangulo);
                    }else if(numeroTmp == 5){
                        client.imagem.setBackgroundResource(R.drawable.losango);
                    }else{
                        client.imagem.setBackgroundResource(R.drawable.triangulo);
                    }
                }
            });

    }


}
