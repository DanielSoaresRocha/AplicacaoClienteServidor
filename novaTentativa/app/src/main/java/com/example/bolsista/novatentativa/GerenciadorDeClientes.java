package com.example.bolsista.novatentativa;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bolsista.novatentativa.arquitetura.Servidor;
import com.example.bolsista.novatentativa.configuracao.Configuracao;
import com.example.bolsista.novatentativa.configuracao.ConfigurarTeste;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.BiConsumer;


public class GerenciadorDeClientes extends Thread{
    private ObjectInputStream leitor;
    private ObjectOutputStream escritor;

    static PrintStream esp32; //enviar comando para o esp32

    private Socket cliente;
    private int numCliente;

    public static Map<Integer,GerenciadorDeClientes> clientes = new HashMap<>();

    private int imgAtual;

    static Jogar jogar;

    private int vetor[];

    private Servidor server;

    public GerenciadorDeClientes(Socket cliente, Servidor server, int numCliente){
        this.cliente = cliente;
        this.server = server;
        this.numCliente = numCliente;
        start();

    }

    @Override
    public void run() {
            if(!identificarCliente()) { //se o cliente não for o esp32
                try {
                    //escritor = new ObjectOutputStream(cliente.getOutputStream());
                    escritor = new ObjectOutputStream(cliente.getOutputStream());
                    Log.i("OBJETO", "Criou output do servidor");
                    sleep(2000);
                    leitor = new ObjectInputStream(cliente.getInputStream());
                    Log.i("OBJETO", "Criou input do servidor");

                    vetor = ConfigurarTeste.configuracao.getImagens();
                    enviarObjeto();

                    int msg;
                    imgAtual = R.drawable.circulo; //////////////////DESTAQUE

                    while (true) {
                        msg = leitor.readInt();
                        imgAtual = server.numberAleatorio;
                        Log.i("COMUNICACAO", "MENSAGEM RECEBIDA DO CLIENTE");
                        Log.i("COMUNICACAO", "cliente -- " + msg + " server = " + imgAtual);

                        if (clientes.size() >= 2) {
                            if (msg == imgAtual) {
                                jogar.tocarAcerto(); // cavalo acertou
                                esperar(); //mudar imagens para branco, e espera um novo sorteio
                                esp32();//enviar comando para o esp32
                                if (!server.controleRemoto) {  // se o controle remoto não estiver conectado
                                    dormir(ConfigurarTeste.configuracao.getIntervalo1()); // tempo de espera do mestre
                                    sortear(); //fazer nova interação de imagens entre os tablets
                                }
                            } else if (msg == 997) {//trocar imagens
                                sortear();
                            } else if (msg == 998) {//fechar socket
                                desconectarControle();
                                break;
                            } else { //O cavalo errou
                                jogar.tocarError();
                            }
                        }
                        //mudarImagem(msg);

                    }
                } catch (IOException e) {
                    Log.i("COMUNICACAO", "ERRO = " + e.getMessage());
                    desconectarCliente();
                }catch (InterruptedException e) {
                    Log.i("ERRO", "ERRO EM SLEEP = " + e.getMessage());
                }
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

    private void desconectarControle() {
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

    private void desconectarCliente(){
        try{
            leitor.close();
            escritor.close();
            cliente.close();

            //clientes.remove(numCliente);//removendo da tabela hash
            clientes.put(numCliente,null);
            jogar.informarDesconexao();

            reestabelecer();
            Log.i("REMOTO", "CLIENTE REMOTO DESCONECTADO");

        }catch (IOException e){
            Log.i("ERRO", "ERRO AO FECHAR CONEXÃO = " + e.getMessage());
        }
    }

    private void reestabelecer(){
        Servidor.numCliente = Servidor.numCliente - 1;//descontar o cliente que foi desconectado
        Map<Integer,GerenciadorDeClientes> clientesAuxiliar = new HashMap<>();

        int x = 0;
        for(int i = 0; i <= 5; i++){
            if(clientes.get(i) == null){
            }else{
                clientesAuxiliar.put(x,clientes.get(i));
                x++;
            }
        }
        clientes = clientesAuxiliar;
        enviarImagemCorreta();

    }

    /* Este metodo nao deixa que a imagem correta se perca com o ultimo cliente que saiu,
    enviando para o primeiro cliente da lista a imagem do servidor
    */
    private void enviarImagemCorreta() {
        if(clientes.size()>0){
            GerenciadorDeClientes destino = clientes.get(0);
            try {
                destino.getEscritor().writeInt(imgAtual);
                destino.getEscritor().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void adicionarCliente() {
        clientes.put(numCliente,this);
        exibirMensagem("pronto para comecar",true);

    }

    //este método faz aparecer o botao começar
    private void exibirMensagem(final String mensagem,final boolean btn) {
        if(clientes.size()>=2){
            server.criarServerBtn.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(server.getApplicationContext(),mensagem,Toast.LENGTH_LONG).show();
                    if(btn){
                    server.comecarServerBtn.setVisibility(View.VISIBLE);
                    }
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
    /*
    Este método trata a comunicação com sockets de uma maneira mais "crua" , a fim de receber a
    mensagem do esp32, é necessário diminuir a abstração que é recebida com ObjectOutputStream e
    ObjectInputStream no java (que recebem um objeto) para os construtores PrintStream(receber dados)
    e  BufferedReader(enviar dados), assim, podemos tratar o protocolo TCP para que o servidor socket
    java receba corretamente os dados de um cliente socket de um esp32

    objetivo: identificar se o tipo do cliente é o controle remoto, um cliente padrão, ou um esp32
    */
    private Boolean identificarCliente() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            Log.i("esp32","Esperando receber mensagem");
            String identificador = entrada.readLine(); // mensagem que é recebida de um tablet ou esp32
            Log.i("esp32","mensagem chegouuuu: "+ identificador);

            if(identificador.contains("padrao")){
                Log.i("REMOTO","CLIENTE PADRAO ADICIONADO");
                exibirMensagem("padrao",false);
                adicionarCliente();
                return false;
            }else if(identificador.contains("remoto")){
                Log.i("REMOTO","CONTROLE REMOTO DETECTADO");
                exibirMensagem("remoto",false);
                server.controleRemoto = true;
                return false;
            }else{
                Log.i("esp32","ESP32 FOI CONECTADO");
                exibirMensagem("esp32",false);
                esp32 = new PrintStream(cliente.getOutputStream());
                esp32.print(1);
                esp32.flush();
                Servidor.numCliente = Servidor.numCliente - 1; //descontar 1
            }
        }catch (IOException e){
            System.out.println("Erro ao identificar cliente: "+ e.getMessage());
            return false;
        }
        return true;
    }

    //enviar comando para o esp32
    public void esp32(){
        try {
        Log.i("ESP32", "ENVIOU COMANDO PARA O ESP");
        esp32.print(1);
        esp32.flush();

        }catch (NullPointerException e){
            Log.i("ERRO", "erro ao enviar comando para o esp32 = "+ e.getMessage());
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
