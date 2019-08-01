package com.example.bolsista.clienteservidor;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends AppCompatActivity {


    Server server;
    Client client;

    Button connect,criarServer;
    EditText host;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servidor);

        connect = findViewById(R.id.button);
        criarServer = findViewById(R.id.button2);
        host = findViewById(R.id.editText);


        System.out.println("PRIMEIRO PRINT");

        criarServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            criarServidor();

            }
        });

        /////////////CONECTAR-SE AO SERVIDOR////////////////////////
        progress = new ProgressDialog(this);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarCliente(host.getText().toString());
/*
                progress.setMessage("Conectando" + host.getText().toString());
                progress.show();

                        //////////
                try {
                    Socket socket = new Socket(host.getText().toString(),5555); //cria conexão entre cliente e server
                    System.out.println("CONSEGUIU??");
                    progress.setMessage("VEREMOS...");
                    //criação dos streams de entrada e saida

                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                    String msg = "HELLO";

                    progress.setMessage("Enviando Mensagem...");
                    output.writeUTF(msg);
                    output.flush();
                    progress.setMessage("Mensagem enviada");

                    msg = input.readUTF();
                    progress.setMessage(msg);

                    input.close();
                    output.close();
                    socket.close();


                }catch (IOException e){
                    progress.setMessage("Erro ao enviar a mensagem");
                    System.out.println("ERRO : "+ e.getMessage());
                }*/

                    }



        });


        /*
        try {
            System.out.println("CRIANDO SERVIDOR...");
            server.criarServerSocket(5555);
            System.out.println("AGUARDANDO CONEXÃO...");
            Socket socket = server.esperaConexao(); //protocolo
            System.out.println("CLIENTE CONECTADO");
            server.trataConexao(socket);
        }catch (IOException e)
        {
            //tratar excessão

            System.out.println(e.getMessage());
        }
        */
    }

    public void criarCliente(String host){
        client = new Client(this, host);
        client.execute();
    }

    public void criarServidor(){
        server = new Server(this);
        server.execute();
    }
}


