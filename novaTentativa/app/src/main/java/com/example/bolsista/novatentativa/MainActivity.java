package com.example.bolsista.novatentativa;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    Button server, client,controleRemotoBtn,comecar;
    EditText host;

    Jogar jogar;
    ServerSocket servidor;

    static Cliente cliente;

    static boolean serverIdentificado = false;
    static boolean controleRemoto = false;

    String numberAleatorio;

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

        controleRemotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarControleRemoto();
            }
        });

        comecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mudarTela = new Intent(MainActivity.this,Jogar.class);

                startActivity(mudarTela);


            }
        });



    }

    private void criarServidor() {
        final MainActivity server = this;
        numberAleatorio = "1";

        serverIdentificado = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Log.i("SERVIDOR", "STARTANDO SERVIDOR");
                    servidor = new ServerSocket(9999);
                    Log.i("SERVIDOR", "Esperando conexão...");

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
        if(host.getText().toString().length() < 5){
            Toast.makeText(getApplicationContext(),"Insira um IP válido!!!", Toast.LENGTH_LONG).show();
        }else {
            final MainActivity client = this;
            cliente = new Cliente(host.getText().toString(),client,false);
            cliente.connect();
            Toast.makeText(getApplicationContext(), "Cliente criado", Toast.LENGTH_SHORT).show();
        }
    }

    public void criarControleRemoto(){
        if(host.getText().toString().length() < 5){
            Toast.makeText(getApplicationContext(),"Insira um IP válido!!!", Toast.LENGTH_LONG).show();
        }else{
            final MainActivity client = this;
            cliente = new Cliente(host.getText().toString(),client,true);
            cliente.connect();
            Toast.makeText(getApplicationContext(), "Controle Ativo", Toast.LENGTH_SHORT).show();

            Intent controleRemoto = new Intent(MainActivity.this,ControleRemoto.class);
            startActivity(controleRemoto);

        }
    }


    public static void enviar2() {
        cliente.escrever();
    }

    public void inicializarComponentes(){
        server = findViewById(R.id.server);
        host = findViewById(R.id.host);
        client = findViewById(R.id.client);
        controleRemotoBtn = findViewById(R.id.controleRemoto);
        comecar = findViewById(R.id.comecar);

    }


}
