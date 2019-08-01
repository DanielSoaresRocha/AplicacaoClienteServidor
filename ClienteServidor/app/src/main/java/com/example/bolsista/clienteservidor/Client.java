package com.example.bolsista.clienteservidor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.IccOpenLogicalChannelResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;

public class Client extends AsyncTask<Void,Void,Void> {
    private Context context;
    private ProgressDialog progress;

    private String host;


    public Client(Context context, String host){
        this.context = context;
        this.host = host;
    }

    @Override //ANTES DE EXECUÇÃO -> ACESSO A THREAD PRINCIPAL
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setMessage("Criando Cliente...");
        progress.show();


    }

    @Override //DURANTE EXECUÇÃO -> ACESSO A OUTRA THREAD
    protected Void doInBackground(Void... params) {
        try{

            Socket socket = new Socket(host,5555); //cria conexão entre cliente e server

            System.out.println("Conectou ao server");
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            System.out.println("Criou as streams entrada e saida");
            String msg = "HELLO";

            System.out.println("Enviando Mensagem...");
            output.writeUTF(msg);
            output.flush();


            msg = input.readUTF();


            input.close();
            output.close();
            if(msg.equals("fechar")){
                socket.close();
            }

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

    }

}
