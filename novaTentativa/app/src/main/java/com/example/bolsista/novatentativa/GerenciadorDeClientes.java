package com.example.bolsista.novatentativa;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;


public class GerenciadorDeClientes extends Thread{

    private Socket cliente;

    private MainActivity server;

    public GerenciadorDeClientes(Socket cliente, MainActivity server){
        this.cliente = cliente;
        this.server = server;
        start();
    }

    @Override
    public void run() {
        try {
            BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter escritor = new PrintWriter(cliente.getOutputStream(),true);
            escritor.println("Cliente foi conectado");
            String msg;

            while (true){
                msg = leitor.readLine();
                Log.i("COMUNICACAO","MENSAGEM RECEBIDA DO CLIENTE");
                mudarImagem();
                escritor.println("Cliente disse: "+msg);
            }



        }catch (IOException e){
            Log.i("COMUNICACAO", "ERRO = "+ e.getMessage());
        }
    }

    private void mudarImagem() {
        server.imagem.post(new Runnable() {
            @Override
            public void run() {
                Random radom  = new Random(); // gerar número aleatório
                int numeroTmp = radom.nextInt(6);

                if(numeroTmp == 0){
                    server.imagem.setBackgroundResource(R.drawable.circulo);
                }else if(numeroTmp == 1){
                    server.imagem.setBackgroundResource(R.drawable.estrela);
                }else if(numeroTmp == 2){
                    server.imagem.setBackgroundResource(R.drawable.hexagono);
                }else if(numeroTmp == 3){
                    server.imagem.setBackgroundResource(R.drawable.retangulo);
                }else if(numeroTmp == 4){
                    server.imagem.setBackgroundResource(R.drawable.triangulo);
                }else if(numeroTmp == 5){
                    server.imagem.setBackgroundResource(R.drawable.losango);
                }else{
                    server.imagem.setBackgroundResource(R.drawable.triangulo);
                }
            }
        });
    }
}
