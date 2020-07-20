package com.example.bolsista.novatentativa.sockets;

import android.content.Context;
import android.util.Log;

import com.example.bolsista.novatentativa.GerenciadorDeClientes;
import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.arquitetura.Servidor;
import com.example.bolsista.novatentativa.modelo.Desafio;
import com.example.bolsista.novatentativa.modelo.Ensaio;
import com.example.bolsista.novatentativa.modelo.Mensagem;
import com.example.bolsista.novatentativa.viewsModels.TesteViewModel;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class PseudoTeste extends PreTeste{
    private static Desafio desafioAtual;
    private static ArrayList<Desafio> desafios = new ArrayList<>();
    public static Map<Integer, Integer> numVezesDesafio = new HashMap<>();

    public PseudoTeste(Socket cliente, int numCliente, Context context) {
        super(cliente, numCliente, context);
        preecherDesafios();
    }

    private void preecherDesafios() {
        desafios = new ArrayList<>();
        desafios.addAll(TesteViewModel.teste.getValue().getDesafios());
        for(int i = 0 ; i < desafios.size(); i++){
            numVezesDesafio.put(i, 0);
        }
    }

    @Override
    public void tratarConexao() throws IOException, ClassNotFoundException {
        int numRodadas = Objects.requireNonNull(TesteViewModel.teste.getValue()).getQtdEnsaiosPorSessao();

        while (rodada < numRodadas){
            Log.i("NUMRODADA", "Rodada de número " + rodada);
            Ensaio ensaio = new Ensaio(); // Iniciando um ensaio
            ensaio.setId(rodada+"");
            msg = (Mensagem) leitor.readObject();
            ensaio.setIdDesafio(Integer.toString(rodada));

            if(clientes.size() >= 1){
                if(msg.getComando() == desafioAtual.getImgCorreta()){
                    jogar.tocarAcerto();
                    rodada++;
                    esp32(ABRIR_MOTOR);
                    esperar();
                    ensaio.setAcerto(true);
                    if (!Servidor.controleRemoto) {  // se o controle remoto não estiver conectado
                        //dormir(TesteViewModel.teste.getValue().getIntervalo1()); // tempo de espera do mestre
                        novaInteracao(); //fazer nova interação de imagens entre os tablets
                        }
                    }else if (msg.getComando() == TROCAR_IMAGENS) {//trocar imagens
                        novaInteracao();
                    }else if (msg.getComando() == FECHAR_SOCKET) {//fechar socket
                        desconectarControle();
                        break;
                    }else { //O cavalo errou
                        jogar.tocarError();
                        ensaio.setAcerto(false);
                    }
                TesteViewModel.sessao.getEnsaios().add(ensaio);
                }
            }
        Log.i("NUMRODADA", "ACABOU =>>> " + rodada);
        TesteViewModel.adicionarNovaSessao();
        terminar();
        jogar.terminar();
    }

    private void novaInteracao(){
        esp32(FECHAR_MOTOR);//enviar comando para o servo fechar no esp32
        sortearDesafio();

        mudarImagem(desafioAtual.getImgCorreta());
        //dormir(TesteViewModel.teste.getValue().getIntervalo2());

        enviarParaEscravos(desafioAtual.getImg1(), desafioAtual.getImg2());
    }

    //mudar imagens para preto, e espera um novo sorteio
    private void esperar() throws IOException{
        mudarImagem(999);

        for(int i = 0; i < clientes.size(); i++){
            PreTeste destino = clientes.get(i);
            destino.getEscritor().writeInt(999);
            destino.getEscritor().flush();
        }
    }

    private static void mudarImagem(int msg) {
        final int comando = msg;
        jogar.imagemButton.post(new Runnable() {
            @Override
            public void run() {
                if(comando == 999){
                    jogar.getImagemButton().setBackgroundResource(R.drawable.tela_preta);
                }else{
                    jogar.getImagemButton().setBackgroundResource(comando);
                }
            }
        });
    }

    public static void sortearDesafio(){
        int desafioDaVez = numeroAleatorio();
        Log.i("PSEUDOTESTE", "Desafio da vez = "+ desafioDaVez);

        desafioAtual = desafios.get(desafioDaVez);
        numVezesDesafio.put(desafioDaVez, numVezesDesafio.get(desafioDaVez)+1);
        if(numVezesDesafio.get(desafioDaVez) >= 5){
            desafios.remove(desafioDaVez);
            reodenarClicksDesafio(desafioDaVez);
        }
    }

    private static void reodenarClicksDesafio(int inicio) {
        for(int i = inicio; i < numVezesDesafio.size()-1; i++){
            numVezesDesafio.put(i,numVezesDesafio.get(i+1));
        }
    }

    //retorna um numero de 0 até o tamanho da lista
    private static int numeroAleatorio() {
        Log.i("NUMRODADA", "ENTROU EM NUMERO ALEATORIO +" + desafios.size());
        Random radom  = new Random(); // gerar número aleatório
        int numeroTmp = radom.nextInt(desafios.size());
        return numeroTmp;
    }

    /*
     Erros que este método pode acarretar:
     1 - Se o servidor começar antes de algum cliente, este respectivo cliente terá um erro
     2 - Se houver menos que dois clientes conectados ao mestre
    */
    public static void comecarInteracao(){
        sortearDesafio();

        enviarParaEscravos(desafioAtual.getImg1(), desafioAtual.getImg2());

        mudarImagem(desafioAtual.getImgCorreta());
    }

    // Enviar comandos para enviar para escravos 1 e 2
    private static void enviarParaEscravos(int img1, int img2){
        try {
            PreTeste destino = clientes.get(0); // primeiro cliente conectado
            destino.getEscritor().writeInt(img1);
            destino.getEscritor().flush();

            destino = clientes.get(1); // segundo cliente conectado
            destino.getEscritor().writeInt(img2);
            destino.getEscritor().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
