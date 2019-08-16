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
import java.util.logging.Handler;


public class GerenciadorDeClientes extends Thread{

    private BufferedReader leitor;
    private PrintWriter escritor;

    private Socket cliente;
    private int numCliente;

    private static final Map<Integer,GerenciadorDeClientes> clientes = new HashMap<>();

    private String imgAtual;

    static Jogar jogar;


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
            //escritor.println("Cliente foi conectado");
            adicionarCliente();
            String msg;

            imgAtual = "1"; //////////////////DESTAQUE

            //sortear();

            while (true){
                msg = leitor.readLine();
                imgAtual = server.numberAleatorio;
                Log.i("COMUNICACAO","MENSAGEM RECEBIDA DO CLIENTE");
                Log.i("COMUNICACAO","cliente -- "+ msg+" server = "+ imgAtual);
                if(clientes.size()>=2){
                    if(msg.equals(imgAtual)){

                        sortear();

                    }
                }
                //mudarImagem(msg);

            }


        }catch (IOException e){
            Log.i("COMUNICACAO", "ERRO = "+ e.getMessage());
        }
    }

    private void adicionarCliente() {
        clientes.put(numCliente,this);
        //clientes.add(this);
    }

    private void mudarImagem(String msg) {
        final String comando = msg;

        jogar.imagemButton.post(new Runnable() {
            @Override
            public void run() {

                if(comando.equals("1")){
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

    }


    public void sortear(){

        //mudar o numero aleatorio no servidor
        server.numberAleatorio = Integer.toString(sortearNumero());
        //colocar este numero na Theread atual
        imgAtual = server.numberAleatorio;

        mudarImagem(imgAtual);

        //sortear o escolhido para herdar imagem
        Random radom  = new Random();
        int clienteEscolhido = radom.nextInt(2);



        Log.i("ENVIAR","ESCOLHIDA = "+ imgAtual);
        for(int i = 0;i < clientes.size(); i++){
            if(i == clienteEscolhido){
                GerenciadorDeClientes destino = clientes.get(i);
                destino.getEscritor().println(imgAtual);
                Log.i("ENVIAR","ENVIADA 1 = "+ imgAtual);
            }else{
                int aleatorio = sortearNumero();
                String outraImg = Integer.toString(aleatorio);
                Log.i("ENVIAR","ENVIADA 2 "+ outraImg);
                GerenciadorDeClientes destino = clientes.get(i);
                destino.getEscritor().println(outraImg);
            }
        }
    }


    private int sortearNumero() {
        Random radom  = new Random(); // gerar número aleatório
        int numeroTmp = radom.nextInt(6);

        return numeroTmp;
    }

    public static void definirTela(Jogar jogarr){
        jogar = jogarr;

    }

    public PrintWriter getEscritor() {
        return escritor;
    }

    public void setEscritor(PrintWriter escritor) {
        this.escritor = escritor;
    }


}
