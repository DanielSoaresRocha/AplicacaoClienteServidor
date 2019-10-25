package com.example.bolsista.novatentativa.arquitetura;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.Cliente;
import com.example.bolsista.novatentativa.GerenciadorDeClientes;
import com.example.bolsista.novatentativa.Jogar;
import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.configuracao.ConfigurarTeste;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends AppCompatActivity {
    Button configTestBtn;
    public Button comecarServerBtn, criarServerBtn;
    LinearLayout serverDiv1,serverDiv2;
    TextView ipTextView;

    static final int PICK_CONTACT_REQUEST = 1;

    ServerSocket servidor;

    public static int numCliente;

    //static Cliente cliente;

    public static int numberAleatorio;
    public static boolean serverIdentificado = false;
    public static boolean controleRemoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servidor);

        inicializar();
        comecar();
        listener();

    }

    private void listener() {
        configTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent configTeste = new Intent(Servidor.this, ConfigurarTeste.class);

                startActivityForResult(configTeste,PICK_CONTACT_REQUEST);

            }
        });

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
        final Servidor server = this;
        numberAleatorio = R.drawable.circulo;

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

    private void comecar() {
        serverDiv2.setVisibility(View.GONE);
        comecarServerBtn.setVisibility(View.GONE);
    }

    private void inicializar() {
        configTestBtn = findViewById(R.id.configTestBtn);
        criarServerBtn = findViewById(R.id.criarServerBtn);
        comecarServerBtn = findViewById(R.id.comecarServerBtn);

        serverDiv1 = findViewById(R.id.serverDiv1);
        serverDiv2 = findViewById(R.id.serverDiv2);

        ipTextView = findViewById(R.id.ipTextView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                serverDiv1.setVisibility(View.GONE);
                serverDiv2.setVisibility(View.VISIBLE);

                Bundle parametro = data.getExtras();
                String desconexao = parametro.getString("desconexao","suficiente");
                if(desconexao.equals("insuficiente")){//se for diferente de suficiente
                    comecarServerBtn.setVisibility(View.GONE);
                }
            }else{
                Toast.makeText(this,"Preencha os campos corretamente",Toast.LENGTH_LONG).show();
            }
        }
    }
}

