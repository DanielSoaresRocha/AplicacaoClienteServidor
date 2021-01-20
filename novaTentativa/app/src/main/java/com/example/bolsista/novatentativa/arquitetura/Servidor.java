package com.example.bolsista.novatentativa.arquitetura;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.GerenciadorDeClientes;
import com.example.bolsista.novatentativa.Jogar;
import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.sockets.PreTeste;
import com.example.bolsista.novatentativa.sockets.PseudoTeste;
import com.example.bolsista.novatentativa.viewsModels.TesteViewModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends AppCompatActivity {
    public static Button comecarServerBtn, criarServerBtn;
    LinearLayout serverDiv2;
    TextView ipTextView;
    public static TextView numEscravo;
    public static LinearLayout numEscravos;
    Context contextActivity;

    ServerSocket servidor;

    public static int numCliente;

    //static Cliente cliente;

    public static int numberAleatorio;
    public static boolean preTeste = false;
    public static boolean controleRemoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servidor);

        inicializar();
        listener();

    }

    private void listener() {
        criarServerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarServidor();
            }
        });

        comecarServerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mudarTela = new Intent(Servidor.this,Jogar.class);
                startActivity(mudarTela);
            }
        });

    }

    private void criarServidor() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        numberAleatorio = R.drawable.tela_preta;

        preTeste = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("SERVIDOR", "STARTANDO SERVIDOR");
                    servidor = new ServerSocket(9999);
                    Log.i("SERVIDOR", "Esperando conexão...");
                    pegarIp();

                    numCliente = 0;
                    while (true) {
                        Socket cliente = servidor.accept();
                        Log.i("SERVIDOR", "CLIENTE FOI CONECTADO = " + cliente.getInetAddress());
                        if(TesteViewModel.teste.getValue().getPreTeste()) // só se não for um pré-teste
                            new PreTeste(cliente,numCliente, contextActivity);
                        else
                            new PseudoTeste(cliente,numCliente, contextActivity);
                        numCliente++;
                    }
                } catch (IOException e) {
                    Log.i("ERRO", "PORTA OCUPADA OU SERVER FOI FECHADO:" + e.getMessage());
                }
            }
        }).start();
    }

    /*private void criarServidor() {
        final Servidor server = this;
        numberAleatorio = R.drawable.branco;

        serverIdentificado = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("SERVIDOR", "STARTANDO SERVIDOR");
                    servidor = new ServerSocket(9999);
                    Log.i("SERVIDOR", "Esperando conexão...");
                    pegarIp();

                    numCliente = 0;
                    while (true) {
                        Socket cliente = servidor.accept();
                        Log.i("SERVIDOR", "CLIENTE FOI CONECTADO = " + cliente.getInetAddress());
                        new GerenciadorDeClientes(cliente, server,numCliente);

                        numCliente++;
                    }
                } catch (IOException e) {
                    Log.i("ERRO", "PORTA OCUPADA OU SERVER FOI FECHADO:" + e.getMessage());
                }
            }
        }).start();
    }*/

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

    //mostrar o ip na tela
    private void mostrarIp(final String ip){
        ipTextView.post(new Runnable() {

            @Override
            public void run() {
                ipTextView.setText(ip);
                ipTextView.setTextColor(getResources().getColor(R.color.vermelhoo));

                Toast.makeText(getApplicationContext(), R.string.servidor_criado,Toast.LENGTH_LONG).show();

            }
        });

    }

    private void inicializar() {
        criarServerBtn = findViewById(R.id.criarServerBtn);
        numEscravos = findViewById(R.id.numEscravos);
        comecarServerBtn = findViewById(R.id.comecarServerBtn);
        serverDiv2 = findViewById(R.id.serverDiv2);
        ipTextView = findViewById(R.id.ipTextView);
        numEscravo = findViewById(R.id.numEscravo);

        comecarServerBtn.setVisibility(View.GONE);
        contextActivity= this;
    }

}

