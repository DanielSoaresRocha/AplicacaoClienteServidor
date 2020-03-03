package com.example.bolsista.novatentativa;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bolsista.novatentativa.arquitetura.Servidor;
import com.example.bolsista.novatentativa.modelo.Desafio;

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
import java.util.Random;


public class GerenciadorDeClientes extends Thread{
    private ObjectInputStream leitor;
    private ObjectOutputStream escritor;

    static PrintStream esp32; //enviar comando para o esp32
    private final int ABRIR_MOTOR = 1;
    private final int FECHAR_MOTOR = 0;
    private final int FECHAR_SOCKET = 998;
    private final int TROCAR_IMAGENS = 997;

    private Socket cliente;
    private int numCliente;

    public static Map<Integer,GerenciadorDeClientes> clientes = new HashMap<>();
    private ArrayList<Desafio> desafios = new ArrayList<>();

    private int imgAtual;

    static Jogar jogar;

    private ArrayList<Integer> vetor;
    int msg;

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
                    escritor = new ObjectOutputStream(cliente.getOutputStream());
                    Log.i("OBJETO", "Criou output do servidor");
                    //dormir(2);
                    leitor = new ObjectInputStream(cliente.getInputStream());
                    Log.i("OBJETO", "Criou input do servidor");
                    //enviarObjeto();
                    imgAtual = R.drawable.circulo; //////////////////DESTAQUE
                    vetor = IniciarConfiguracao.configuracaoSelecionada.getImagens();
                    int numRodadas = IniciarConfiguracao.configuracaoSelecionada.getQtdQuestoes();
                    int rodada = 1;
                    Desafio desafio = new Desafio();
                    while (rodada <= numRodadas) {
                        msg = leitor.readInt();
                        imgAtual = server.numberAleatorio;
                        desafio.setImgCorreta(imgAtual);
                        Log.i("COMUNICACAO", "MENSAGEM RECEBIDA DO CLIENTE");
                        Log.i("COMUNICACAO", "cliente -- " + msg + " server = " + imgAtual);
                        if (clientes.size() >= 2) {
                            if (msg == imgAtual) {
                                jogar.tocarAcerto(); // cavalo acertou
                                esperar(); //mudar imagens para branco, e espera um novo sorteio
                                esp32(ABRIR_MOTOR);//enviar comando para abrir o servo no esp32
                                if (!server.controleRemoto) {  // se o controle remoto não estiver conectado
                                    dormir(IniciarConfiguracao.configuracaoSelecionada.getIntervalo1()); // tempo de espera do mestre
                                    sortear(); //fazer nova interação de imagens entre os tablets
                                }
                            } else if (msg == TROCAR_IMAGENS) {//trocar imagens
                                sortear();
                            } else if (msg == FECHAR_SOCKET) {//fechar socket
                                desconectarControle();
                                break;
                            } else { //O cavalo errou
                                desafio.setQtdErros(desafio.getQtdErros()+1);
                                jogar.tocarError();
                            }
                        }
                        rodada++;
                        desafios.add(desafio);
                    }
                    terminar();
                    jogar.terminar(desafios, IniciarConfiguracao.experimento.getId());
                } catch (IOException e) {
                    Log.i("COMUNICACAO", "ERRO = " + e.getMessage());
                    desconectarCliente();
                }/*catch (InterruptedException e) {
                    Log.i("ERRO", "ERRO EM SLEEP = " + e.getMessage());
                }*/
            }
    }

    private void enviarObjeto() {
        try {
            escritor.writeObject(IniciarConfiguracao.configuracaoSelecionada);
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
            //clientes.remove(numCliente);//removendo da tabela hash
            clientes.put(numCliente,null);
            enviarImagemCorreta();
            reestabelecer();
            jogar.informarDesconexao();
            Log.i("REMOTO", "CLIENTE PADRÃO DESCONECTADO");

            leitor.close();
            escritor.close();
            cliente.close();

        }catch (IOException e){
            Log.i("ERRO", "ERRO AO FECHAR CONEXÃO = " + e.getMessage());
        }catch (java.lang.NullPointerException e){
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
    }

    /* Este metodo nao deixa que a imagem correta se perca com o ultimo cliente que saiu,
    enviando para o primeiro cliente da lista a imagem do servidor
    */
    private void enviarImagemCorreta() {
        for(int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i) != null) {
                GerenciadorDeClientes destino = clientes.get(i);
                try {
                    destino.getEscritor().writeInt(Servidor.numberAleatorio);
                    destino.getEscritor().flush();
                    Log.i("COMUNICACAO","Enviou imagem correta para o cliente");
                } catch (IOException e) {
                    Log.i("ERRO", "Erro ao enviar imagem correta: "+ e.getMessage());
                }
                break;
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
                Servidor.numCliente = Servidor.numCliente - 1; //descontar 1
            }
        }catch (IOException e){
            System.out.println("Erro ao identificar cliente: "+ e.getMessage());
            return false;
        }
        return true;
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

    public void sortear() throws IOException {
        esp32(FECHAR_MOTOR);//enviar comando para o servo fechar no esp32

        //mudar o numero aleatorio no servidor
        server.numberAleatorio = vetor.get(sortearNumero());
        //colocar este numero na Theread atual
        imgAtual = server.numberAleatorio;

        mudarImagem(imgAtual);

        dormir(IniciarConfiguracao.configuracaoSelecionada.getIntervalo2());

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

                while (vetor.get(aleatorio) == server.numberAleatorio){
                    //este laço não deixa o número do outro tablet ser igual
                    aleatorio = sortearNumero();
                }

                int outraImg = vetor.get(aleatorio);
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
        int numeroTmp = radom.nextInt(vetor.size());
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

    // Informar e fechar todos os sockets (clientes)
    private void terminar() throws IOException {
        for(int i = 0; i < clientes.size(); i++){
            GerenciadorDeClientes destino = clientes.get(i);
            destino.getEscritor().writeInt(900);// comando para fechar socket no lado do cliente
            destino.getEscritor().flush();

            destino.getEscritor().close();
            destino.getLeitor().close();
            destino.getCliente().close();
        }
    }

    public ObjectOutputStream getEscritor() {
        return escritor;
    }

    public void setEscritor(ObjectOutputStream escritor) {
        this.escritor = escritor;
    }

    public ObjectInputStream getLeitor() {
        return leitor;
    }

    public Socket getCliente() {
        return cliente;
    }
}
