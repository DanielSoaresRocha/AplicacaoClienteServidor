package com.example.bolsista.novatentativa;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bolsista.novatentativa.arquitetura.ClienteActivity;
import com.example.bolsista.novatentativa.configuracao.Configuracao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {

    private ObjectInputStream leitor;//****
    private ObjectOutputStream escritor;//*****

    private String host;


    private Socket cliente;
    private ClienteActivity client;
    static Jogar jogar;

    private int imgAtual;

    private Boolean controleRemoto;

    public Cliente(String host, ClienteActivity client, boolean controleRemoto){
        this.host = host;
        this.client = client;
        this.controleRemoto = controleRemoto;
    }


    public void connect(){
        imgAtual = 1; //////////////////DESTAQUE

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
        cliente = new Socket(host,9999);



        //lendo mensagens do servidor
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    leitor = new ObjectInputStream(cliente.getInputStream());
                    Log.i("OBJETO","Criou input do CLIENTE");
                    receberObjeto();

                    while (true){
                        int mensagem = leitor.readInt(); ///******
                        Log.i("COMUNICACAO","MENSAGEM RECEBIDA DO SERVER ="+ mensagem);

                        mudarImagem(mensagem);
                    }

                }catch (IOException e){
                    Log.i("ERRO","IMPOSSÍVEL LER MENSAGEM");
                }
            }
        }).start();

            //criando escrita

            escritor = new ObjectOutputStream(cliente.getOutputStream());
            Log.i("OBJETO","Criou output do CLIENTE");


            if(!controleRemoto){
                escritor.writeUTF("CLIENTE PADRÃO");
                escritor.flush();
            }else{
                escritor.writeUTF("remoto");
                escritor.flush();
            }

            ativarBotao();
        }catch (IOException e){
            Log.i("ERRO","ERRO AO CONECTAR-SE AO SERVIDOR "+ e.getMessage());
        }
    }

    private void receberObjeto() {
        try {
            Configuracao configuracao = (Configuracao) leitor.readObject();
            Log.i("OBJETO","Objeto recebido do servidor ");
            Log.i("OBJETO","vetor = "+ configuracao.getQtdQuestoes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    //escrevendo para o servidor - cliente Padrão
    public void escrever(){
        try{
            Log.i("COMUNICACAO","MENSAGEM ENVIADA AO SERVIDOR:  "+ imgAtual);
            escritor.writeInt(imgAtual);
            escritor.flush();

        }catch (IOException e){
            Log.i("ERRO", "erro ao enviar mensagem ao servidor" + e.getMessage());
        }
    }

    public void controleRemoto(){
        try{
            Log.i("COMUNICACAO", "CONTROLE REMOTO ACIONADO");
            escritor.writeInt(997);
            escritor.flush();

        }catch (IOException e){
            Log.i("ERRO", "erro ao enviar no controle remoto" + e.getMessage());
        }

    }

    public void desconect(){

        try{
            escritor.writeInt(998);

            escritor.close();
            leitor.close();

            cliente.close();

            Log.i("REMOTO","CONEXÃO COM SERVIDOR FECHADA");
        }catch (IOException e){
            Log.i("ERRO", "ERRO AO FECHAR CLIENTE = " + e.getMessage());
        }

    }


    private void mudarImagem(int msg) {
        final int comando = msg;

            jogar.imagemButton.post(new Runnable() {
                @Override
                public void run() {
                    if(comando == 999){
                        jogar.getImagemButton().setBackgroundResource(R.drawable.branco);
                    }else{
                        jogar.getImagemButton().setBackgroundResource(comando);
                    }
                }
            });

            imgAtual = msg;
    }

    private void ativarBotao() {
        client.comecarClientBtn.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(client.getApplicationContext(),R.string.comecar,Toast.LENGTH_LONG).show();
                client.comecarClientBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void definirTela(Jogar jogarr){
        jogar = jogarr;

    }

}
