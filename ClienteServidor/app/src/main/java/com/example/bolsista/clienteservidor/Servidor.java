package com.example.bolsista.clienteservidor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends AppCompatActivity {

    private ServerSocket serverSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servidor);

        System.out.println("PRIMEIRO PRINT");

        try {
            System.out.println("CRIANDO SERVIDOR...");
            criarServerSocket(5555);
            System.out.println("AGUARDANDO CONEXÃO...");
            Socket socket = esperaConexao(); //protocolo
            System.out.println("CLIENTE CONECTADO");
            trataConexao(socket);
        }catch (IOException e)
        {
            //tratar excessão

            System.out.println(e.getMessage());
        }
    }

    private void criarServerSocket(int porta) throws IOException
    {
        serverSocket = new ServerSocket(porta);

    }
    private Socket esperaConexao() throws IOException
    {
        System.out.println("TENTANDO");
        Socket socket = serverSocket.accept();
        System.out.println("PASSOU");
        return socket;
    }

    private void fechaSocket(Socket s) throws IOException{
        s.close();
    }

    private void trataConexao(Socket socket) throws IOException
    {
        //protocolo da aplicação
        try{
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        /*Cliente --> hello
          Server <-- HELLO WORLD
        */
        String msg = input.readUTF();
        System.out.println("MENSAGEM RECEBIDA");
        output.writeUTF("HELLO WORLD");

        input.close();
        output.close();
        }catch (IOException e)
        {
            //tratamento de falhas
        }finally {
            //final do tratamento do protocolo
            fechaSocket(socket);
        }
    }
}
