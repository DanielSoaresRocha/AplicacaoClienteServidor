package com.example.bolsista.novatentativa.sockets;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bolsista.novatentativa.GerenciadorDeClientes;
import com.example.bolsista.novatentativa.Jogar;
import com.example.bolsista.novatentativa.NovoExperimento;
import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.arquitetura.Definir;
import com.example.bolsista.novatentativa.arquitetura.Servidor;
import com.example.bolsista.novatentativa.modelo.Desafio;
import com.example.bolsista.novatentativa.modelo.Mensagem;
import com.example.bolsista.novatentativa.viewsModels.TesteViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
* Esta é a classe responsável pelo pré teste;
* */

public class PreTeste extends Thread {
    private ObjectInputStream leitor; // ler dados
    private ObjectOutputStream escritor;// enviar dados

    //comandos para o exp32
    static PrintStream esp32; // enviar dados para o esp
    private final int ABRIR_MOTOR = 1;
    private final int FECHAR_MOTOR = 0;
    private final int FECHAR_SOCKET = 998;
    private final int TROCAR_IMAGENS = 997;

    // informações do cliente socket
    private Socket cliente; // o cliente recebido nesta Thread
    private int numCliente; // o número do cliente conectado

    //lista global que irá conter todos os clientes conectados
    public static Map<Integer,PreTeste> clientes = new HashMap<>();

    //interações
    private int imgAtual;
    int msg;
    Context context;
    static Jogar jogar;

    public PreTeste(Socket cliente, int numCliente, Context context){
        this.cliente = cliente;
        this.numCliente = numCliente;
        this.context = context;
        start();
    }

    @Override
    public void run() {
        if(!identificarCliente()) { //se o cliente não for o esp32
            try {
                escritor = new ObjectOutputStream(cliente.getOutputStream());
                enviarObjeto();
                Log.i("OBJETO", "Criou output do servidor");
                leitor = new ObjectInputStream(cliente.getInputStream());
                Log.i("OBJETO", "Criou input do servidor");

                imgAtual = R.drawable.branco;
                int numRodadas = Objects.requireNonNull(TesteViewModel.teste.getValue()).getQtdEnsaiosPorSessao();
                int rodada = 1;
                while (rodada <= numRodadas){
                    Log.i("OBJETO", "Entrou no Wile - esperando mensagem do cliente...");
                    msg = leitor.readInt();
                    Log.i("OBJETO", "leu mensagem "+ msg);

                    if(clientes.size() >= 1){
                        jogar.tocarAcerto(); // cavalo acertou
                        esp32(ABRIR_MOTOR);
                        dormir(5);
                        esp32(FECHAR_MOTOR);//enviar comando para o servo fechar no esp32
                    }
                    rodada++;
                }
                terminar();
                jogar.terminar();
            } catch (IOException e) {
                Log.i("COMUNICACAO", "ERRO = " + e.getMessage());
                desconectarCliente();
            }
        }
    }

    private void enviarObjeto() throws IOException{
        Mensagem mensagem = new Mensagem(numCliente, 0);
        escritor.writeObject(mensagem);
        escritor.flush();
        Log.i("OBJETO","Enviou objeto" + mensagem.getIdentificacao());
    }

    private void dormir(int tempo){
        try {
            tempo *= 1000; //transforma segundos em millisegundos
            sleep(tempo);

        } catch (InterruptedException e) {
            Log.i("ERRO", "ERRO EM SLEEP = " + e.getMessage());
        }
    }

    // Informar e fechar todos os sockets (clientes)
    private void terminar() throws IOException {
        for(int i = 0; i < clientes.size(); i++){
            PreTeste destino = clientes.get(i);
            destino.getEscritor().writeInt(900);// comando para fechar socket no lado do cliente
            destino.getEscritor().flush();

            destino.getEscritor().close();
            destino.getLeitor().close();
            destino.getCliente().close();
        }
    }

    private void desconectarCliente(){
        try{
            clientes.remove(numCliente);//removendo da tabela hash
            jogar.informarDesconexao(clientes.size());

            leitor.close();
            escritor.close();
            cliente.close();
        }catch (IOException e){
            Log.i("ERRO", "ERRO AO FECHAR CONEXÃO = " + e.getMessage());
        }catch (java.lang.NullPointerException e){
            Log.i("ERRO", "ERRO AO FECHAR CONEXÃO = " + e.getMessage());
        }
    }

    //enviar comando para o esp32, 1 para abrir o motor, e 0 para fechar
    public void esp32(int comando){
        if(esp32 != null) {
            try {
                esp32.print(comando);
                esp32.flush();
                Log.i("enviarESP32", "ENVIOU COMANDO PARA O ESP");
            } catch (NullPointerException e) {
                Log.i("ERRO", "erro ao enviar comando para o esp32 = " + e.getMessage());
            }
        }
    }

    // Identificar se o cliente é um esp32, controleRemoto ou um cliente padrão
    private Boolean identificarCliente() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            Log.i("esp32","Esperando receber mensagem");
            String identificador = entrada.readLine(); // mensagem que é recebida de um tablet ou esp32
            Log.i("esp32","mensagem chegouuuu: "+ identificador);

            if(identificador.contains("padrao")){
                enviarMensagem("Padrão conectado");
                adicionarCliente();
                return false;
            }else if(identificador.contains("remoto")){
                enviarMensagem("Remoto conectado");
                Servidor.controleRemoto = true;
                return false;
            }else{
                Log.i("esp32","ESP32 FOI CONECTADO");
                enviarMensagem("Esp32 conectado");
                esp32 = new PrintStream(cliente.getOutputStream());
                Servidor.numCliente = Servidor.numCliente - 1; //VERIFICAR FUNCIONAMENTO
            }
        }catch (IOException e){
            Log.i("esp32","Erro ao identificar cliente: "+ e.getMessage());
            return false;
        }
        return true;
    }

    private void adicionarCliente() {
        clientes.put(numCliente,this);
        //se houver mais do que dois tablets conectados:
        if(clientes.size()>=2){
            Servidor.criarServerBtn.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Pronto para começar", Toast.LENGTH_SHORT).show();
                    Servidor.comecarServerBtn.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void enviarMensagem(String mensagem){
        Servidor.criarServerBtn.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
                Servidor.comecarServerBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void definirTela(Jogar jogarr){
        jogar = jogarr;
    }

    public ObjectOutputStream getEscritor() {
        return escritor;
    }

    public ObjectInputStream getLeitor() {
        return leitor;
    }

    public Socket getCliente() {
        return cliente;
    }
}
