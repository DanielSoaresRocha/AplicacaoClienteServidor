package com.example.bolsista.clienteservidor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Space;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends AsyncTask<Void,String,Socket>{
    private Context context;
    private ProgressDialog progress;

    private ServerSocket serverSocket;

    public Server(Context context){
        this.context = context;
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
    protected Socket doInBackground(Void... params) {
        Socket socket;
        try {
            progress.setMessage("esperando conexão de cliente");
            socket = esperaConexao();
            progress.setMessage("Cliente foi conectado");

            //////////////TRATAMENTO DO PROTOCOLO/////////////////////
            trataConexao(socket);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }

    return socket;
    }

    @Override //ATUALIZAÇÃO -> É OPCIONAL
    protected void onProgressUpdate(String... params) {

    }

    @Override //DEPOIS DA EXECUÇÃO
    protected void onPostExecute(Socket params) {
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


    public void trataConexao(Socket socket) throws IOException
    {
        //protocolo da aplicação
        try{
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        //Cliente --> hello
          //Server <-- HELLO WORLD
            progress.setMessage("Esperando mensagem");
            String msg = input.readUTF();

            System.out.println("MENSAGEM RECEBIDA");
            progress.setMessage("MENSAGEM RECEBIDA");

            output.writeUTF("HELLO WORLD");

            input.close();
            output.close();
        }catch (IOException e)
        {
            System.out.println("DEU ERRO");
            //tratamento de falhas
        }finally {
            //final do tratamento do protocolo
            fechaSocket(socket);
        }
    }

    public void fechaSocket(Socket s) throws IOException{
        s.close();
    }

}
