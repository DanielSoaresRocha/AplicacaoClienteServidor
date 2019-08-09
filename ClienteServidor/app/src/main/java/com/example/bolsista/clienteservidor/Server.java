package com.example.bolsista.clienteservidor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.TreeMap;

public class Server extends AsyncTask<Void,String,Void>{
    private Context context;
    private ProgressDialog progress;
    private Servidor servidor;

    Socket socket;


    private String msg;


    public Server(Context context, Servidor servidor,Socket socket){
        this.context = context;
        this.servidor = servidor;
        this.socket = socket;
    }

    @Override //DURANTE EXECUÇÃO -> ACESSO A OUTRA THREAD
    protected Void doInBackground(Void... params) {
        trataConexao(socket);
        return null;
    }

    public void trataConexao(Socket socket)
    {
        //protocolo da aplicação
        try{
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            System.out.println("Criou as strenams");

            msg = "";
            while(!(msg.equals("sair"))){
                msg = input.readUTF();
                System.out.println("mensagem do cliente: "+ msg);

                mudaImagem();
                System.out.println("Mudou imagem");

                output.writeUTF("Servidor:2");
                output.flush();


            }

            //input.close();
            //output.close();
        }catch (IOException e)
        {
            System.out.println("DEU ERRO NO TRATAMENTO");

            System.out.println("ERRO = " + e.getMessage());
            //tratamento de falhas
        }finally {
            //final do tratamento do protocolo
            //fechaSocket(socket);

        }
    }


    public void mudaImagem(){
        servidor.imagem.post(new Runnable() {
            @Override
            public void run() {

                Random radom  = new Random(); // gerar número aleatório
                int numeroTmp = radom.nextInt(6);

                if(numeroTmp == 0){
                    servidor.imagem.setBackgroundResource(R.drawable.circulo);
                }else if(numeroTmp == 1){
                    servidor.imagem.setBackgroundResource(R.drawable.estrela);
                }else if(numeroTmp == 2){
                    servidor.imagem.setBackgroundResource(R.drawable.hexagono);
                }else if(numeroTmp == 3){
                    servidor.imagem.setBackgroundResource(R.drawable.retangulo);
                }else if(numeroTmp == 4){
                    servidor.imagem.setBackgroundResource(R.drawable.triangulo);
                }else if(numeroTmp == 5){
                    servidor.imagem.setBackgroundResource(R.drawable.losango);
                }else{
                    servidor.imagem.setBackgroundResource(R.drawable.triangulo);
                }

                //servidor.imagem.setBackgroundResource(R.drawable.circulo);
            }
        });
    }

}

