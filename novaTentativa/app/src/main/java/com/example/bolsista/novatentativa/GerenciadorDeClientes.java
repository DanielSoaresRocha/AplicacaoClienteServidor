package com.example.bolsista.novatentativa;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bolsista.novatentativa.configuracao.Configuracao;
import com.example.bolsista.novatentativa.configuracao.ConfigurarTeste;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class GerenciadorDeClientes extends Thread{
    private ObjectInputStream leitor;
    private ObjectOutputStream escritor;

    private Socket cliente;
    private int numCliente;

    private static final Map<Integer,GerenciadorDeClientes> clientes = new HashMap<>();

    private int imgAtual;

    static Jogar jogar;


    private int vetor[];

    private MainActivity server;

    public GerenciadorDeClientes(Socket cliente, MainActivity server, int numCliente){
        this.cliente = cliente;
        this.server = server;
        this.numCliente = numCliente;
        start();

    }

    @Override
    public void run() {
        try {
            escritor = new ObjectOutputStream(cliente.getOutputStream());///***
            Log.i("OBJETO","Criou output do servidor");///****
            leitor = new ObjectInputStream(cliente.getInputStream());///*****
            Log.i("OBJETO","Criou input do servidor");///****

            vetor = ConfigurarTeste.configuracao.getImagens();

            identificarCliente();

            int msg;
            imgAtual = 1; //////////////////DESTAQUE
            enviarObjeto();

            while (true){
                msg = leitor.readInt();
                imgAtual = server.numberAleatorio;
                Log.i("COMUNICACAO","MENSAGEM RECEBIDA DO CLIENTE");
                Log.i("COMUNICACAO","cliente -- "+ msg+" server = "+ imgAtual);

                if(clientes.size()>=2){
                    if(msg == imgAtual){
                        jogar.tocarAcerto(); // cavalo acertou

                        esperar(); //mudar imagens para branco, e espera um novo sorteio
                        if(!server.controleRemoto){  // se o controle remoto não estiver conectado
                            dormir(ConfigurarTeste.configuracao.getIntervalo1()); // tempo de espera do mestre
                            sortear(); //fazer nova interação de imagens entre os tablets
                        }
                    }else if(msg == 997){
                        sortear();
                    }else if(msg == 998){
                        desconectar();
                        break;
                    }else{ //O cavalo errou
                        jogar.tocarError();
                    }
                }
                //mudarImagem(msg);

            }


        }catch (IOException e){
            Log.i("COMUNICACAO", "ERRO = "+ e.getMessage());
        }
    }

    private void enviarObjeto() {
        try {
            escritor.writeObject(ConfigurarTeste.configuracao);
            escritor.flush();

            Log.i("OBJETO","OBJETO ENVIADO PARA O CLIENTE");
        } catch (IOException e) {
            Log.i("ERRO","ERRO AO ENVIAR OBJETO" + e.getMessage());
        }
    }

    private void desconectar() {
        try{
            server.controleRemoto = false;

            leitor.close();
            escritor.close();
            cliente.close();

            Log.i("REMOTO", "CONTROLE REMOTO DESCONECTADO");

        }catch (IOException e){
            Log.i("ERRO", "ERRO AO FECHAR CONEXÃO = " + e.getMessage());
        }
    }


    private void adicionarCliente() {
        clientes.put(numCliente,this);
        ativarBotao();

    }

    //este método faz aparecer o botao começar
    private void ativarBotao() {
        if(clientes.size()>=2){
            server.comecar.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(server.getApplicationContext(),R.string.comecar,Toast.LENGTH_LONG).show();
                    server.comecar.setVisibility(View.VISIBLE);
                }
            });
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

    }

    private void identificarCliente() {
        try {

        String identificaCliente = leitor.readUTF();

        if(identificaCliente.equals("remoto")){
            Log.i("REMOTO","CONTROLE REMOTO DETECTADO");
            server.controleRemoto = true;
        }else{
            Log.i("REMOTO","CLIENTE PADRAO ADICIONADO");
            adicionarCliente();
        }


        }catch (IOException e){
            System.out.println("Erro ao identificar cliente: "+ e.getMessage());
        }
    }

    public void sortear() throws IOException {

        //mudar o numero aleatorio no servidor
        server.numberAleatorio = vetor[sortearNumero()];
        //colocar este numero na Theread atual
        imgAtual = server.numberAleatorio;

        mudarImagem(imgAtual);

        dormir(ConfigurarTeste.configuracao.getIntervalo2());

        //sortear o escolhido para herdar imagem
        Random radom = new Random();
        int clienteEscolhido = radom.nextInt(clientes.size());

        Log.i("ENVIAR","ESCOLHIDA = "+ imgAtual);
        for(int i = 0;i < clientes.size(); i++){
            if(i == clienteEscolhido){
                GerenciadorDeClientes destino = clientes.get(i);
                destino.getEscritor().writeInt(imgAtual);
                destino.getEscritor().flush();
                Log.i("ENVIAR","ENVIADA 1 = "+ imgAtual);
            }else{
                int aleatorio = sortearNumero();

                while (vetor[aleatorio] == server.numberAleatorio){
                    //este laço não deixa o número do outro tablet ser igual
                    aleatorio = sortearNumero();
                }

                int outraImg = vetor[aleatorio];
                Log.i("ENVIAR","ENVIADA 2 "+ outraImg);

                GerenciadorDeClientes destino = clientes.get(i);
                destino.getEscritor().writeInt(outraImg);
                destino.getEscritor().flush();
            }
        }
    }

    private void dormir(int tempo){
        try {
            tempo *= 1000; //transforma segundos em millisegundos
            sleep(tempo);

        } catch (InterruptedException e) {
            Log.i("ERRO", "ERRO EM SLEEP = " + e.getMessage());
        }
    }

    private int sortearNumero() {
        Random radom  = new Random(); // gerar número aleatório
        int numeroTmp = radom.nextInt(vetor.length);

        return numeroTmp;
    }

    private void esperar() throws IOException{
        mudarImagem(999);

        for(int i = 0; i < clientes.size(); i++){
            GerenciadorDeClientes destino = clientes.get(i);
            destino.getEscritor().writeInt(999);
            destino.getEscritor().flush();
        }
    }

    public static void definirTela(Jogar jogarr){
        jogar = jogarr;

    }

    public ObjectOutputStream getEscritor() {
        return escritor;
    }

    public void setEscritor(ObjectOutputStream escritor) {
        this.escritor = escritor;
    }


}
