package com.example.bolsista.clienteservidor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.IccOpenLogicalChannelResponse;
import android.util.Log;
import android.widget.Space;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;

public class Server extends AsyncTask<Void,String,Void>{
    private Context context;
    private ProgressDialog progress;
    private Servidor servidor;

    private ServerSocket serverSocket;

    public Server(Context context, Servidor servidor){
        this.context = context;
        this.servidor = servidor;
    }

    @Override //ANTES DE EXECUÇÃO -> ACESSO A THREAD PRINCIPAL
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Criando servidor...");
        progress.show();

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
            System.out.println("CRIANDO STREAMS DE ENTRADA E SAIDA");
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        //Cliente --> hello
          //Server <-- HELLO WORLD
            System.out.println("Criou as strenams");

            String msg = input.readUTF();

            System.out.println("MENSAGEM RECEBIDA");
            Log.i("TESTE","A mensagem foi recebida: "+ msg);

            output.writeUTF("HELLO CLIENT");
            output.flush(); //limpar o buffer -> diz quando terminou a mensagem

            input.close();
            output.close();
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

}
