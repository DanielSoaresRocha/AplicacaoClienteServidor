package com.example.bolsista.clienteservidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

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

    public void fechaSocket(Socket s) throws IOException{
        s.close();
    }

    public void trataConexao(Socket socket) throws IOException
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
