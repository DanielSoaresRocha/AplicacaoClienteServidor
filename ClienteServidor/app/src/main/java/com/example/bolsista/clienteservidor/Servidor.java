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

    Button connect;
    EditText host;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servidor);

        System.out.println("PRIMEIRO PRINT");
        criarServidor();

        /////////////CONECTAR-SE AO SERVIDOR////////////////////////
        progress = new ProgressDialog(this);

        connect = findViewById(R.id.button);
        host = findViewById(R.id.editText);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                progress.setMessage("Conectando ao servidor...");
                progress.show();
                Socket socket = new Socket(host.getText().toString(),5555); //cria conexão entre cliente e server

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
                    System.out.println(e.getMessage());
                }
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

    public void criarServidor(){
        server = new Server(this);
        server.execute();
    }
}


