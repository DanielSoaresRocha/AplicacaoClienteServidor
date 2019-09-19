package com.example.bolsista.novatentativa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.configuracao.ConfigurarTeste;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {
    Button server, client,controleRemotoBtn,comecar, configurarBtn;
    EditText host;
    ServerSocket servidor;

    static Cliente cliente;

    static boolean serverIdentificado = false;
    static boolean controleRemoto = false;

    int numberAleatorio;


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

        configurarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaConfiguracao = new Intent(MainActivity.this, ConfigurarTeste.class);
                startActivity(telaConfiguracao);
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
        numberAleatorio = 1;

        serverIdentificado = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Log.i("SERVIDOR", "STARTANDO SERVIDOR");
                    servidor = new ServerSocket(9999);
                    Log.i("SERVIDOR", "Esperando conexão...");
                    pegarIp();

                    int numCliente = 0;
                    while (true) {
                        Socket cliente = servidor.accept();
                        Log.i("SERVIDOR", "CLIENTE FOI CONECTADO = " + cliente.getInetAddress());
                        //new GerenciadorDeClientes(cliente,server,numCliente);

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
            Toast.makeText(getApplicationContext(),R.string.ip_invalido, Toast.LENGTH_LONG).show();
        }else {
            final MainActivity client = this;
            //cliente = new Cliente(host.getText().toString(),client,false);
            cliente.connect();
            Toast.makeText(getApplicationContext(), R.string.cliente_criado, Toast.LENGTH_SHORT).show();
        }
    }

    public void criarControleRemoto(){
        if(host.getText().toString().length() < 5){
            Toast.makeText(getApplicationContext(),R.string.ip_invalido, Toast.LENGTH_LONG).show();
        }else{
            final MainActivity client = this;
            //cliente = new Cliente(host.getText().toString(),client,true);
            cliente.connect();
            Toast.makeText(getApplicationContext(), R.string.controle_ativo, Toast.LENGTH_SHORT).show();

            Intent controleRemoto = new Intent(MainActivity.this,ControleRemoto.class);
            startActivity(controleRemoto);

        }
    }


    public static void enviar2() {
        cliente.escrever();
    }

    //este método retorna o ip de conexão wifi no android
    private void pegarIp(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String strIP = String.format("%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));

        System.out.println("endereço ip = "+ strIP);
        mostrarIp(strIP);

    }

    //este método recebe o ip e atualiza no textView
    private void mostrarIp(final String ip){
        host.post(new Runnable() {

            @Override
            public void run() {
                host.setText(ip);
                host.setTextColor(getResources().getColor(R.color.vermelhoo));

                Toast.makeText(getApplicationContext(), R.string.servidor_criado,Toast.LENGTH_LONG).show();

            }
        });

    }

    public void inicializarComponentes(){
        server = findViewById(R.id.server);
        host = findViewById(R.id.host);
        client = findViewById(R.id.client);
        controleRemotoBtn = findViewById(R.id.controleRemoto);
        comecar = findViewById(R.id.comecar);
        configurarBtn = findViewById(R.id.configurarBtn);

    }




}
