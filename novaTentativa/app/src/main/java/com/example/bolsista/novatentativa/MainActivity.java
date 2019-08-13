package com.example.bolsista.novatentativa;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    Button server, client,enviar,imagem;
    EditText host;


    ServerSocket servidor;

    Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentes();

        server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarServidor();
            }
        });

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarCliente();
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliente.escrever("1");
            }
        });


    }

    private void criarServidor() {
        final MainActivity server = this;


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Log.i("SERVIDOR", "STARTANDO SERVIDOR");
                    servidor = new ServerSocket(9999);
                    Log.i("SERVIDOR", "Esperando conexão...");
                    //Toast.makeText(getApplicationContext(), "Esperando...", Toast.LENGTH_LONG).show();
                    int numCliente = 0;
                    while (true) {
                        Socket cliente = servidor.accept();
                        Log.i("SERVIDOR", "CLIENTE FOI CONECTADO = " + cliente.getInetAddress());
                        new GerenciadorDeClientes(cliente,server,numCliente);
                        numCliente++;
                    }

                } catch (IOException e) {
                    Log.i("ERRO", "PORTA OCUPADA OU SERVER FOI FECHADO:" + e.getMessage());
                }


            }
        }).start();
    }


    public void criarCliente(){
        final MainActivity client = this;
        cliente = new Cliente(host.getText().toString(),client);
        cliente.connect();
        Toast.makeText(getApplicationContext(), "Cliente criado", Toast.LENGTH_SHORT).show();

    }

    public void inicializarComponentes(){
        server = findViewById(R.id.server);
        host = findViewById(R.id.host);
        client = findViewById(R.id.client);
        enviar = findViewById(R.id.enviar);
        imagem = findViewById(R.id.imagem);

    }


}
