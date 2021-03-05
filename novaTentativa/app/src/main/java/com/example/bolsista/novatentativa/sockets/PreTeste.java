package com.example.bolsista.novatentativa.sockets;

import android.annotation.SuppressLint;
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
import com.example.bolsista.novatentativa.modelo.Ensaio;
import com.example.bolsista.novatentativa.modelo.Mensagem;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.example.bolsista.novatentativa.viewsModels.TesteViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
* Esta é a classe responsável pelo pré teste;
*
* ATUALIZAÇÃO : A melhor solução é usar o BufferedReader para entrada e um PrintWriter para saída, em vez de fluxos.
* */

public class PreTeste extends Thread {
    public BufferedReader leitor; // ler dados
    private PrintWriter escritor;// enviar dados

    //comandos para o exp32
    static PrintStream esp32; // enviar dados para o esp
    public final int ABRIR_MOTOR = 1;
    public final int FECHAR_MOTOR = 0;
    public final int FECHAR_SOCKET = 998;
    public final int TROCAR_IMAGENS = 997;

    // informações do cliente socket
    private Socket cliente; // o cliente recebido nesta Thread
    private int numCliente; // o número do cliente conectado

    //lista global que irá conter todos os clientes conectados
    public static Map<Integer,PreTeste> clientes = new HashMap<>();
    public static Map<Integer, Integer> numClicks = new HashMap<>();

    //interações
    public String msg[];
    private Context context;
    static Jogar jogar;
    public static int rodada = 1;

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
                escritor = new PrintWriter(cliente.getOutputStream(), true);
                leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                Log.i("OBJETO", "Criou output do servidor");
                Log.i("OBJETO", "Criou input do servidor");
                enviarObjeto();
                incrementaEscravos();
                tratarConexao();

            } catch (IOException e) {
                Log.i("COMUNICACAO", "ERRO = " + e.getMessage());
                desconectarCliente();
            } catch (ClassNotFoundException e) {
                Log.i("COMUNICACAO", "Não foi possível ler o objeto = " + e.getMessage());
            }
        }
    }

    public void tratarConexao() throws IOException, ClassNotFoundException {
        int numRodadas = Objects.requireNonNull(TesteViewModel.teste.getValue()).getQtdEnsaiosPorSessao();
        while (rodada <= numRodadas){
            Ensaio ensaio = new Ensaio(); // Iniciando um ensaio
            ensaio.setId(rodada+"");
            Log.i("OBJETO", "Entrou no While - esperando mensagem do cliente...");
            msg = leitor.readLine().split("-");
            Log.i("OBJETO", "leu mensagem "+ msg[0]);
            ensaio.setIdDesafio(Integer.toString(rodada));

            if(clientes.size() >= 1){
                int numClicksClient = numClicks.get(Integer.parseInt(msg[0]));
                //Se este cliente tiver clicado menos a quantidada max de vezes consecutivas
                Log.i("Cliente "+ msg[0], "clicks = "+ numClicksClient);
                if(numClicksClient < TesteViewModel.teste.getValue().getMaxVezesConsecutivas()){
                    jogar.tocarAcerto(); // cavalo acertou
                    esp32(ABRIR_MOTOR);
                    //esp32(FECHAR_MOTOR);//enviar comando para o servo fechar no esp32numClicks.put(msg.)
                    numClicks.put(Integer.parseInt(msg[0]), numClicksClient+1);
                    rodada++;
                    //ensaio
                    ensaio.setAcerto(true);
                }else {
                    jogar.tocarError();
                    Log.i("TAXA_ACERTO", "CAVALO ERROU - ADICIONANDO ERRO");
                    ensaio.setAcerto(false);
                }
                TesteViewModel.sessao.getEnsaios().add(ensaio);
            }
        }
        TesteViewModel.adicionarNovaSessao();
        terminar();
        jogar.terminar();
    }


    //incrementar número de escravos conectados na activity servidor
    private void incrementaEscravos() {
        Servidor.criarServerBtn.post(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                Servidor.numEscravos.setVisibility(View.VISIBLE);
                Servidor.numEscravo.setText(Integer.toString(clientes.size()));
                Servidor.comecarServerBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void enviarObjeto() throws IOException{
        Mensagem mensagem = new Mensagem(numCliente, 0);
        //escritor.writeObject(mensagem);
        escritor.println(numCliente+"-"+0);
        escritor.flush();
        Log.i("OBJETO","Enviou objeto" + mensagem.getIdentificacao());
    }

    public static void dormir(int tempo){
        try {
            tempo *= 1000; //transforma segundos em millisegundos
            sleep(tempo);

        } catch (InterruptedException e) {
            Log.i("ERRO", "ERRO EM SLEEP = " + e.getMessage());
        }
    }

    // Informar e fechar todos os sockets (clientes)
    public void terminar() throws IOException {
        Log.i("TERMINAR", "QTD DE CLIENTE CONECTADOS:" + clientes.size());
        for(int i = 0; i < clientes.size(); i++){
            PreTeste destino = clientes.get(i);
            destino.getEscritor().println("900");// comando para fechar socket no lado do cliente
            destino.getEscritor().flush();

            destino.getEscritor().close();
            destino.getLeitor().close();
            destino.getCliente().close();
        }

    }

    private void desconectarCliente(){
        try{
            //clientes.remove(numCliente);//removendo da tabela hash
            //Servidor.numCliente--;// descontar 1
            jogar.informarDesconexao(clientes.size());

            escritor.close();
            leitor.close();
            cliente.close();
        }catch (IOException e){
            Log.i("ERRO", "ERRO AO FECHAR CONEXÃO = " + e.getMessage());
        }catch (java.lang.NullPointerException e){
            Log.i("ERRO", "ERRO AO FECHAR CONEXÃO = " + e.getMessage());
        }
    }

    public void desconectarControle() {
        try{
            Servidor.controleRemoto = false;
            leitor.close();
            escritor.close();
            cliente.close();
            Log.i("REMOTO", "CONTROLE REMOTO DESCONECTADO");
        }catch (IOException e){
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
        removerThreadsParadas();
        clientes.put(numCliente,this);// incrementa mais um cliente na lista de clientes conectados
        numClicks.put(numCliente, 0);//este cliente inicia com 0 clicks

        //se houver mais do que dois tablets conectados:
        if(clientes.size()>=2){
            Servidor.criarServerBtn.post(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(context, "Pronto para começar", Toast.LENGTH_SHORT).show();
                    Servidor.comecarServerBtn.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    //remover as threads que não deram certo
    private void removerThreadsParadas() {
        for(int i = 0; i < clientes.size(); i++){
            try {
                if(clientes.get(i).getLeitor() == null){
                    clientes.get(i).cliente.close();
                    clientes.remove(i);
                    Servidor.numCliente--;
                    numCliente = numCliente -1;
                }
            }catch (NullPointerException | IOException e){
                Log.i("THREAD", "ENTROU NA EXCESSÃO");
                clientes.remove(i);
                Servidor.numCliente--;
                numCliente = numCliente -1;
            }
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

    public BufferedReader getLeitor() {
        return leitor;
    }

    public void setLeitor(BufferedReader leitor) {
        this.leitor = leitor;
    }

    public PrintWriter getEscritor() {
        return escritor;
    }

    public void setEscritor(PrintWriter escritor) {
        this.escritor = escritor;
    }

    public Socket getCliente() {
        return cliente;
    }
}
