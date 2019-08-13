package com.example.bolsista.novatentativa;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class GerenciadorDeClientes extends Thread{

    private BufferedReader leitor;
    private PrintWriter escritor;

    private Socket cliente;
    private static final List<GerenciadorDeClientes> clientes = new ArrayList<GerenciadorDeClientes>();
    private int numCliente;

    private MainActivity server;

    public GerenciadorDeClientes(Socket cliente, MainActivity server, int numCliente){
        this.cliente = cliente;
        this.server = server;
        this.numCliente = numCliente;
        start();
    }

    @Override
    public void run() {
        try {
            leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            escritor = new PrintWriter(cliente.getOutputStream(),true);
            escritor.println("Cliente foi conectado");
            adicionarCliente();
            String msg;

            while (true){
                msg = leitor.readLine();
                Log.i("COMUNICACAO","MENSAGEM RECEBIDA DO CLIENTE");
                mudarImagem();
                escritor.println("Cliente disse: "+msg);

                if(msg.equals("1")){
                    Log.i("TESTE","ENVIANDO MENSAGEM PARA OUTRO TABLET...");
                    for(int i = 0;i <= clientes.size()-1; i++){
                        if(i == numCliente){
                            continue;
                        }else{
                            GerenciadorDeClientes destino = clientes.get(i);
                            destino.getEscritor().println(msg);
                        }
                    }
                }
            }



        }catch (IOException e){
            Log.i("COMUNICACAO", "ERRO = "+ e.getMessage());
        }
    }

    private void adicionarCliente() {
        //clientes.put(numCliente,this);
        clientes.add(this);
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

    public PrintWriter getEscritor() {
        return escritor;
    }

    public void setEscritor(PrintWriter escritor) {
        this.escritor = escritor;
    }
}
