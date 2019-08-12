package com.example.bolsista.novatentativa;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class GerenciadorDeClientes extends Thread{

    private Socket cliente;

    public GerenciadorDeClientes(Socket cliente){
        this.cliente = cliente;
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
                escritor.println("Cliente disse: "+msg);
            }



        }catch (IOException e){
            Log.i("COMUNICACAO", "ERRO = "+ e.getMessage());
        }
    }
}
