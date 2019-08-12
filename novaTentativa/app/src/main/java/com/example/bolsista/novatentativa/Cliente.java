package com.example.bolsista.novatentativa;

import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

    private String host;

    private PrintWriter escritor;

    private Socket cliente;

    public Cliente(String host){
        this.host = host;
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

    }


}
