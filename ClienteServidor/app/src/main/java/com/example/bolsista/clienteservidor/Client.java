package com.example.bolsista.clienteservidor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.IccOpenLogicalChannelResponse;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Random;

public class Client extends AsyncTask<Void,Void,Void> {
    private Context context;
    private ProgressDialog progress;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private Servidor servidor;
    String msg;

    private String host;


    public Client(Context context, String host, Servidor servidor){
        this.context = context;
        this.host = host;
        this.servidor = servidor;
    }

    @Override //ANTES DE EXECUÇÃO -> ACESSO A THREAD PRINCIPAL
    protected void onPreExecute() {
        //progress = new ProgressDialog(context);
        //progress.setMessage("Criando Cliente...");
        //progress.show();


    }

    @Override //DURANTE EXECUÇÃO -> ACESSO A OUTRA THREAD
    protected Void doInBackground(Void... params) {
        try{

            Socket socket = new Socket(host,5555); //cria conexão entre cliente e server
            System.out.println("Conectou ao server");

            trataConexao(socket);

            //input.close();
           // output.close();
            //socket.close();
        }catch (IOException e){
            System.out.println("Erro no cliente ="+ e.getMessage());
        }

        return null;
    }

    @Override //ATUALIZAÇÃO -> É OPCIONAL
    protected void onProgressUpdate(Void... params) {

    }

    @Override //DEPOIS DA EXECUÇÃO
    protected void onPostExecute(Void params) {
        //progress.dismiss();
    }

    public void trataConexao(Socket socket){
        try {
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        System.out.println("Criou as streams entrada e saida");


        Log.i("TESTE","CLIENTE E SERVIDOR ESTÃO CONECTADOS");
        }catch (IOException e){
            System.out.println("Deu erro no tratamento do cliente: "+ e.getMessage());
        }


    }

    public void enviar(){
        System.out.println("Entrou no metodo enviar");
        msg = "1";

        new Thread(new Runnable() {
            @Override
            public void run() {


            try {
                output.writeUTF(msg);//envia para o server
                output.flush();////limpar o buffer

                System.out.println("mensagem enviada para o servidor");
                Log.i("TESTE","Mensagem enviada para o servidor");

                mudaImagem();

                msg = input.readUTF();//receber do server
                System.out.println("mensagem recebida do servidor:" + msg);
                Log.i("TESTE","Mensagem recebida do servidor");

            //input.close();
            //output.close();

        }catch (IOException e){
                System.out.println("Erro na thread -" + e.getMessage());
        }
            }
        }).start();

    }

    public void mudaImagem(){
        servidor.imagem.post(new Runnable() {
            @Override
            public void run() {


                Random radom  = new Random(); // gerar número aleatório
                int numeroTmp = radom.nextInt(6);

                if(numeroTmp == 0){
                    servidor.imagem.setBackgroundResource(R.drawable.circulo);
                }else if(numeroTmp == 1){
                    servidor.imagem.setBackgroundResource(R.drawable.estrela);
                }else if(numeroTmp == 2){
                    servidor.imagem.setBackgroundResource(R.drawable.hexagono);
                }else if(numeroTmp == 3){
                    servidor.imagem.setBackgroundResource(R.drawable.retangulo);
                }else if(numeroTmp == 4){
                    servidor.imagem.setBackgroundResource(R.drawable.triangulo);
                }else if(numeroTmp == 5){
                    servidor.imagem.setBackgroundResource(R.drawable.losango);
                }else{
                    servidor.imagem.setBackgroundResource(R.drawable.triangulo);
                }

                //servidor.imagem.setBackgroundResource(R.drawable.circulo);
            }
        });
    }
}
