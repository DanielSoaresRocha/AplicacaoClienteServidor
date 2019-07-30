package com.example.bolsista.clienteservidor;

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


    Server server = new Server();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servidor);

        System.out.println("PRIMEIRO PRINT");

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
    }


}
