package com.example.bolsista.clienteservidor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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


public class Server extends AsyncTask<Void,String,Void>{
    private Context context;
    private ProgressDialog progress;
    private Servidor servidor;

    private ObjectOutputStream output;
    private ObjectInputStream input;

    private String msg;


    private ServerSocket serverSocket;

    public Server(Context context, Servidor servidor){
        this.context = context;
        this.servidor = servidor;
    }

    @Override //ANTES DE EXECUÇÃO -> ACESSO A THREAD PRINCIPAL
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        // progress.setMessage("Criando servidor...");--------
        //progress.show();--------

        try {
            criarServerSocket(5555);
            System.out.println("SERVIDOR INICIADO NA PORTA 5555");
            progress.setMessage("Iniciado na porta 5555");


        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override //DURANTE EXECUÇÃO -> ACESSO A OUTRA THREAD
    protected Void doInBackground(Void... params) {
        Socket socket;
        try {

            while(true){


            Log.i("TESTE","ESPERANDO CONEXÃO..");

            socket = esperaConexao();
            Log.i("TESTE","CLIENTE FOI CONECTADO = "+ socket.getInetAddress());

            //////////////TRATAMENTO DO PROTOCOLO/////////////////////
            Log.i("TESTE","Vai entrar em tratamento");

            trataConexao(socket);

            }
        } catch (IOException e) {
            Log.i("ERRO","Erro no servidor: "+ e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    @Override //ATUALIZAÇÃO -> É OPCIONAL
    protected void onProgressUpdate(String... params) {

    }

    @Override //DEPOIS DA EXECUÇÃO
    protected void onPostExecute(Void params) {
        progress.setMessage("realizado");
        progress.dismiss();
    }


    public void criarServerSocket(int porta) throws IOException
    {
        serverSocket = new ServerSocket(porta);

    }
    public Socket esperaConexao() throws IOException
    {
        System.out.println("TENTANDO");
        Socket socket = serverSocket.accept();
        System.out.println("PASSOU");
        return socket;
    }


    public void trataConexao(Socket socket)
    {
        //protocolo da aplicação
        try{

            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
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

    public void fechaSocket(Socket s) throws IOException{
        s.close();
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
