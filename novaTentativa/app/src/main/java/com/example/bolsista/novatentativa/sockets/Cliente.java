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
import com.example.bolsista.novatentativa.arquitetura.Remoto;
import com.example.bolsista.novatentativa.modelo.Mensagem;
import com.example.bolsista.novatentativa.modelo.Teste;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public final int FECHAR_SOCKET = 998;
    public final int TROCAR_IMAGENS = 997;

    private BufferedReader leitor;//****
    private PrintWriter escritor;//*****
    private int indentificador;

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
        imgAtual = R.drawable.tela_preta;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);// permitir que execute algum comando sem precisar criar uma nova thread.

        try {
            cliente = new Socket(host, 9999);
            escritor = new PrintWriter(cliente.getOutputStream(), true);
            Log.i("OBJETO", "Criou output do CLIENTE");
            leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            Log.i("OBJETO", "Criou input do CLIENTE");
            enviarIdentificacao();

        }catch (IOException e){
            Log.i("ERRO","ERRO AO CONECTAR-SE AO SERVIDOR "+ e.getMessage());
        }
    }


    private void ouvirServidor(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    receberObjeto();
                    //Irá receber infinitamente mensagens do servidor
                    while (true){
                        //int mensagem = leitor.readInt(); // o loop fica pausado aqui até que receba algum comando do servidor
                        String mensagem = leitor.readLine();
                        Log.i("COMUNICACAO","MENSAGEM RECEBIDA DO SERVER = "+ mensagem);
                        int comando = Integer.parseInt(mensagem);
                        mudarImagem(comando);
                    }

                }catch (IOException e){
                    Log.i("ERRO","IMPOSSÍVEL LER MENSAGEM "+ e.getMessage());
                    closeSocket();
                }catch (NumberFormatException e){
                    Log.i("ERRO","IMPOSSÍVEL LER MENSAGEM "+ e.getMessage());
                    closeSocket();
                }
            }
        }).start();
    }

    private void receberObjeto() {
        try {
            Log.i("OBJETO","Esperando receber objeto do server");
            String mensagem[] = leitor.readLine().split("-");
            Log.i("OBJETO","Objeto recebido do servidor =  identificacao " + mensagem[0]);

            indentificador = Integer.parseInt(mensagem[0]);

            if(!controleRemoto) {
                final ClienteActivity cliente = (ClienteActivity) client;

                cliente.identificacaoCliente.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        cliente.identificacaoCliente.setVisibility(View.VISIBLE);
                        cliente.numIdentificacao.setText(Integer.toString(Integer.parseInt(mensagem[0]) + 1));
                        Toast.makeText(client.getApplicationContext(), R.string.comecar, Toast.LENGTH_LONG).show();
                        cliente.comecarClientBtn.setVisibility(View.VISIBLE);
                        cliente.criarClientBtn.setVisibility(View.GONE);
                        cliente.infoIpText.setTextColor(cliente.getResources().getColor(R.color.verde));
                        cliente.infoIpText.setText("Conectado");
                        cliente.ipClientEdit.setVisibility(View.GONE);
                    }
                });
            }else{
                final Remoto remoto = (Remoto) client;

                remoto.remotoEditText.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(client.getApplicationContext(), R.string.comecar, Toast.LENGTH_LONG).show();
                        remoto.comecarRemotoBtn.setVisibility(View.VISIBLE);
                        remoto.criarRemotoBtn.setVisibility(View.GONE);
                        remoto.remotoEditText.setVisibility(View.GONE);
                        remoto.infoIpTextRemoto.setText("Conectado");
                        remoto.infoIpTextRemoto.setTextColor(remoto.getResources().getColor(R.color.verde));
                    }
                });
            }
        } catch (IOException e) {
            Log.i("OBJETO", "ERRO AO TENTAR RECEBER OBJETO:"+ e.getMessage());
        }
    }


    //escrevendo para o servidor - cliente Padrão
    public void escrever(){
        Log.i("COMUNICACAO","MENSAGEM ENVIADA AO SERVIDOR:  "+ imgAtual);
        //escritor.writeObject(new Mensagem(indentificador, imgAtual));
        escritor.println(indentificador+"-"+imgAtual);
        //escritor.writeInt(imgAtual);
        escritor.flush();
    }

    //este método envia para o servidor um comando do controle remoto: 997 = mudar imagem
    public void controleRemoto(){
            //escritor.writeObject(new Mensagem(indentificador, TROCAR_IMAGENS));
            escritor.println(indentificador+"-"+TROCAR_IMAGENS);
            escritor.flush();
            Log.i("COMUNICACAO", "CONTROLE REMOTO ACIONADO");
    }

    public void desconect(){
        escritor.println(indentificador+"-"+FECHAR_SOCKET);
        escritor.flush();

        closeSocket();
    }

    private void closeSocket(){

        try {
            escritor.close();
            leitor.close();
            cliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void mudarImagem(int msg) {
        final int comando = msg;

            jogar.imagemButton.post(new Runnable() {
                @Override
                public void run() {
                    if(comando == 999){
                        jogar.getImagemButton().setBackgroundResource(R.drawable.tela_preta);
                    }else if(comando == 900){
                        jogar.terminar();
                    }else{
                        jogar.getImagemButton().setBackgroundResource(comando);
                    }
                }
            });

            imgAtual = msg;
    }

    private void ativarBotao() {
        final ClienteActivity cliente = (ClienteActivity) client;

        cliente.ipClientEdit.post(new Runnable() {
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
       // PrintWriter saida = new PrintWriter(cliente.getOutputStream());
        if(!controleRemoto){
            escritor.println("padrao");
            escritor.flush();
        }else{
            escritor.println("remoto");
            escritor.flush();
        }
        //saida.close();///deve-se fechar se não nao irá conseguir usar essa ponte de novo
        ouvirServidor();
    }

    public static void definirTela(Jogar jogarr){
        jogar = jogarr;
    }

}
