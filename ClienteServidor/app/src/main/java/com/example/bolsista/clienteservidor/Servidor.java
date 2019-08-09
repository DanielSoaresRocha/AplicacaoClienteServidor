package com.example.bolsista.clienteservidor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends AppCompatActivity {

    //Server server;
    Client client;
    Button connect,criarServer, enviar, imagem;
    EditText host;

    Socket socket;
    Handler handler;


    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servidor);

        handler = new Handler();
        imagem = findViewById(R.id.img);
        connect = findViewById(R.id.button);
        criarServer = findViewById(R.id.button2);
        host = findViewById(R.id.editText);
        enviar = findViewById(R.id.enviarServer);


        System.out.println("PRIMEIRO PRINT");

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.enviar();

            }
        });

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

                    }
        });

    }

    public void criarCliente(String host){
        client = new Client(this, host,this);
        client.execute();
    }

    /*public void criarServidor(){
        server = new Server(this,this, handler);
        server.onPreExecute();
    }*/

    public void criarServidor(){
        final Context context = this;
        final Servidor servidor = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(5555);

                    while (true){
                        Log.i("TESTE","ESPERANDO CONEXÃO..");
                        Socket socket = serverSocket.accept();
                        Log.i("TESTE","CLIENTE FOI CONECTADO = "+ socket.getInetAddress());


                        Server server = new Server(context,servidor,socket);
                        server.execute();
                        Thread.sleep(2000);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }).start();



    }
    public void indicarIp(String ip){
        host.setText("ip");
    }
}


