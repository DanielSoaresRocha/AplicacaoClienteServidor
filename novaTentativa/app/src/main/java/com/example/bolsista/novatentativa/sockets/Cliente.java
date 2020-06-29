package com.example.bolsista.novatentativa.sockets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bolsista.novatentativa.Jogar;
import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.arquitetura.ClienteActivity;
import com.example.bolsista.novatentativa.modelo.Mensagem;
import com.example.bolsista.novatentativa.modelo.Teste;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;


/*
* Esta classe representa o cliente socket, ela é responsável por ser o tablet que apenas irá receber
* o comando para mudar a sua imagem. Ela também poder ser o controle remoto, um tipo de cliente socket
* que poderá enviar algum comando especial para o servidor socket. Portanto deve-se escolher qual tipo
* de cliente esta classe será através do parâmentro "controleRemoto" presente no construtor.
* */
public class Cliente {

    private ObjectInputStream leitor;//****
    private ObjectOutputStream escritor;//*****

    private String host;


    private Socket cliente;
    private Activity client;
    static Jogar jogar;

    private int imgAtual;

    private Boolean controleRemoto;

    public Cliente(String host, Activity client, boolean controleRemoto){
        this.host = host;
        this.client = client;
        this.controleRemoto = controleRemoto;
    }

    public void connect(){
        imgAtual = R.drawable.circulo;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);// permitir que execute algum comando sem precisar criar uma nova thread.

        try {
            cliente = new Socket(host,9999);
            enviarIdentificacao();
            escritor = new ObjectOutputStream(cliente.getOutputStream());
            Log.i("OBJETO","Criou output do CLIENTE");
            escritor.writeInt(0);
            escritor.flush();// ESTÁ LINHA É EXTREMAMENTE IMPORTANTE PARA O SERVIDOR CONSEGUIR LER OS DADOS DO CLIENTE;
            leitor = new ObjectInputStream(cliente.getInputStream());
            receberObjeto();
            Log.i("OBJETO","Criou input do CLIENTE");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Irá receber infinitamente mensagens do servidor
                    while (true){
                        int mensagem = leitor.readInt(); // o loop fica pausado aqui até que receba algum comando do servidor
                        Log.i("COMUNICACAO","MENSAGEM RECEBIDA DO SERVER ="+ mensagem);

                        mudarImagem(mensagem);
                    }

                }catch (IOException e){
                    Log.i("ERRO","IMPOSSÍVEL LER MENSAGEM");
                }
            }
        }).start();
        if(!controleRemoto){
            ativarBotao();
        }
        }catch (IOException e){
            Log.i("ERRO","ERRO AO CONECTAR-SE AO SERVIDOR "+ e.getMessage());
        }
    }

    private void receberObjeto() {
        try {
            Mensagem mensagem = (Mensagem) leitor.readObject();
            Log.i("OBJETO","Objeto recebido do servidor =  identificacao " + mensagem.getIdentificacao());

            final ClienteActivity cliente = (ClienteActivity) client;

            cliente.identificacaoCliente.post(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    cliente.identificacaoCliente.setVisibility(View.VISIBLE);
                    cliente.numIdentificacao.setText(Integer.toString(mensagem.getIdentificacao()+1));
                }
            });
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

    //este método envia para o servidor um comando do controle remoto: 997 = mudar imagem
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
        final ClienteActivity cliente = (ClienteActivity) client;

        cliente.comecarClientBtn.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(client.getApplicationContext(),R.string.comecar,Toast.LENGTH_LONG).show();
                cliente.comecarClientBtn.setVisibility(View.VISIBLE);
            }
        });

    }
    /*
    Da mesma forma que o servidor recebe a comunicação de uma forma mais crua para identificação,
    o cliente também deve reduzir o nível de abstração utilizando para enviar a mensagem PrintStream
    ao invés de ObjectOutputStream
    */
    public void enviarIdentificacao() throws IOException{
        //PrintStream saida = new PrintStream(cliente.getOutputStream());
        PrintWriter saida = new PrintWriter(cliente.getOutputStream());
        if(!controleRemoto){
            saida.println("padrao");
            saida.flush();
        }else{
            saida.println("remoto");
            saida.flush();
        }
        //saida.close();///deve-se fechar se não nao irá conseguir usar essa ponte de novo
    }

    public static void definirTela(Jogar jogarr){
        jogar = jogarr;
    }

}
