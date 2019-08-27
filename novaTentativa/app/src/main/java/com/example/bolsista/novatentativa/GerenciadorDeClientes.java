package com.example.bolsista.novatentativa;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Handler;


public class GerenciadorDeClientes extends Thread{
    private ObjectInputStream leitor;
    private ObjectOutputStream escritor;


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
            escritor = new ObjectOutputStream(cliente.getOutputStream());///***
            Log.i("OBJETO","Criou output do servidor");///****
            leitor = new ObjectInputStream(cliente.getInputStream());///*****
            Log.i("OBJETO","Criou input do servidor");///****

            identificarCliente();

            String msg;
            imgAtual = "1"; //////////////////DESTAQUE


            while (true){
                msg = leitor.readUTF();
                imgAtual = server.numberAleatorio;
                Log.i("COMUNICACAO","MENSAGEM RECEBIDA DO CLIENTE");
                Log.i("COMUNICACAO","cliente -- "+ msg+" server = "+ imgAtual);
                if(clientes.size()>=2){
                    if(msg.equals(imgAtual)){

                        esperar(); //mudar imagens para branco, e espera um novo sorteio
                        if(!server.controleRemoto){  // se o controle remoto não estiver conectado
                            sleep(5000); // tempo de espera de 5 segundos
                            sortear(); //fazer nova interação de imagens entre os tablets
                        }
                    }

                    if(msg.equals("mudar")){
                        sortear();
                    }else if(msg.equals("desconect")){
                        desconectar();
                        break;
                    }
                }
                //mudarImagem(msg);

            }


        }catch (IOException e){
            Log.i("COMUNICACAO", "ERRO = "+ e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void desconectar() {
        try{
            server.controleRemoto = false;

            leitor.close();
            escritor.close();
            cliente.close();

            Log.i("REMOTO", "CONTROLE REMOTO DESCONECTADO");

        }catch (IOException e){
            Log.i("ERRO", "ERRO AO FECHAR CONEXÃO = " + e.getMessage());
        }
    }


    private void adicionarCliente() {
        clientes.put(numCliente,this);

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

    }

    private void identificarCliente() {
        try {

        String identificaCliente = leitor.readUTF();

        if(identificaCliente.equals("remoto")){
            Log.i("REMOTO","CONTROLE REMOTO DETECTADO");
            server.controleRemoto = true;
        }else{
            Log.i("REMOTO","CLIENTE PADRAO ADICIONADO");
            adicionarCliente();
        }


        }catch (IOException e){
            System.out.println("Erro ao identificar cliente: "+ e.getMessage());
        }
    }

    public void sortear() throws IOException{

        //mudar o numero aleatorio no servidor
        server.numberAleatorio = Integer.toString(sortearNumero());
        //colocar este numero na Theread atual
        imgAtual = server.numberAleatorio;

        mudarImagem(imgAtual);

        //sortear o escolhido para herdar imagem
        Random radom  = new Random();
        int clienteEscolhido = radom.nextInt(2);

        try{
            sleep(5000);

        }catch (InterruptedException e){
            Log.i("ERRO","ERRO EM SLEEP = "+ e.getMessage());
        }

        Log.i("ENVIAR","ESCOLHIDA = "+ imgAtual);
        for(int i = 0;i < clientes.size(); i++){
            if(i == clienteEscolhido){
                GerenciadorDeClientes destino = clientes.get(i);
                destino.getEscritor().writeUTF(imgAtual);
                destino.getEscritor().flush();
                Log.i("ENVIAR","ENVIADA 1 = "+ imgAtual);
            }else{
                int aleatorio = sortearNumero();

                while (aleatorio == Integer.parseInt(server.numberAleatorio)){
                    //este laço não deixa o número do outro tablet ser igual
                    aleatorio = sortearNumero();
                }

                String outraImg = Integer.toString(aleatorio);
                Log.i("ENVIAR","ENVIADA 2 "+ outraImg);

                GerenciadorDeClientes destino = clientes.get(i);
                destino.getEscritor().writeUTF(outraImg);
                destino.getEscritor().flush();
            }
        }
    }


    private int sortearNumero() {
        Random radom  = new Random(); // gerar número aleatório
        int numeroTmp = radom.nextInt(6);

        return numeroTmp;
    }

    private void esperar() throws IOException{
        mudarImagem("branco");

        for(int i = 0; i < clientes.size(); i++){
            GerenciadorDeClientes destino = clientes.get(i);
            destino.getEscritor().writeUTF("branco");
            destino.getEscritor().flush();
        }
    }

    public static void definirTela(Jogar jogarr){
        jogar = jogarr;

    }

    public ObjectOutputStream getEscritor() {
        return escritor;
    }

    public void setEscritor(ObjectOutputStream escritor) {
        this.escritor = escritor;
    }


}
