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

public class PseudoTeste extends PreTeste {
    private static Desafio desafioAtual;
    private static ArrayList<Desafio> desafios = new ArrayList<>();

    // testes com 4 desafios
    private static String desafioAnterior = "";
    private static int numRepeticoes = 0;

    // testes com 8 desafios
    private static int desafios1 = 0, desafios2 = 0, ladoE = 0, ladoD = 0;

    public PseudoTeste(Socket cliente, int numCliente, Context context) {
        super(cliente, numCliente, context);
        preecherDesafios();
    }

    private void preecherDesafios() {
        desafios = new ArrayList<>();
        desafios.addAll(TesteViewModel.teste.getValue().getDesafios());
    }

    @Override
    public void tratarConexao() throws IOException, ClassNotFoundException {
        int numRodadas = Objects.requireNonNull(TesteViewModel.teste.getValue()).getQtdEnsaiosPorSessao();

        while (rodada <= numRodadas) {
            Ensaio ensaio = new Ensaio(); // Iniciando um ensaio
            ensaio.setId(rodada + "");
            msg = (Mensagem) leitor.readObject();
            ensaio.setIdDesafio(Integer.toString(rodada));

            if (msg.getComando() == desafioAtual.getImgCorreta()) {
                jogar.tocarAcerto();
                rodada++;
                esp32(ABRIR_MOTOR);
                ensaio.setAcerto(true);
                if (!Servidor.controleRemoto && rodada <= numRodadas) {
                    //dormir(TesteViewModel.teste.getValue().getIntervalo1()); // tempo de espera do mestre
                    novaInteracao(); //fazer nova interação de imagens entre os tablets
                }
            } else if (msg.getComando() == TROCAR_IMAGENS) {//trocar imagens
                novaInteracao();
            } else if (msg.getComando() == FECHAR_SOCKET) {//fechar socket
                desconectarControle();
                break;
            } else { //O cavalo errou
                jogar.tocarError();
                ensaio.setAcerto(false);
            }
            TesteViewModel.sessao.getEnsaios().add(ensaio);
        }
        TesteViewModel.adicionarNovaSessao();
        terminar();
        jogar.terminar();
    }

    private void novaInteracao() {
        esp32(FECHAR_MOTOR);//enviar comando para o servo fechar no esp32

        sortearDesafio();

        mudarImagem(desafioAtual.getImgCorreta());
        //dormir(TesteViewModel.teste.getValue().getIntervalo2());

        enviarParaEscravos(desafioAtual.getImg1(), desafioAtual.getImg2());
    }

    //mudar imagens para preto, e espera um novo sorteio
    private void esperar() throws IOException {
        mudarImagem(999);

        for (int i = 0; i < clientes.size(); i++) {
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
                if (comando == 999) {
                    jogar.getImagemButton().setBackgroundResource(R.drawable.tela_preta);
                } else {
                    jogar.getImagemButton().setBackgroundResource(comando);
                }
            }
        });
    }

    private static void sortearDesafio() {
        if (desafios.size() > 4) // Testes L2, T1 e T2
            l2();
        else // somente o teste L1, L3
            l1();

    }

    private static void l2() {
        int desafioDaVez;

        desafioDaVez = numeroAleatorio(0, desafios.size() - 1); // pega um desafio aleatório de 0 a 7

        if(desafioDaVez < 4){ // se for menor que 4 desafios1 é incrementado
            desafios1++;
            Log.i("DEBUGL2", "Desafios 1 apareceram " + desafios1 +" vezes");
            if(desafioDaVez == 0 || desafioDaVez == 2)// posicoes onde o lado direito é correto
                ladoD++;
            else
                ladoE++;
        }else{// se não, desafios2 é incrementado
            desafios2++;
            Log.i("DEBUGL2", "Desafios 2 apareceram " + desafios2 +" vezes");
            if(desafioDaVez == 4 || desafioDaVez == 6)// posicoes onde o lado direito é correto
                ladoD++;
            else
                ladoE++;
        }

        // se algum destes chegar a 10, somente o outro será chamado
        if(desafios1 > 10){
            Log.i("DEBUGL22", "Desafios 1 ultrapassaram 10, agora somente desafios 2");
            desafioDaVez = numeroAleatorio(4, desafios.size() - 1);
        }else if (desafios2 > 10){
            desafioDaVez = numeroAleatorio(0,3);
            Log.i("DEBUGL22", "Desafios 2 ultrapassaram 10, agora somente desafios 1");
        }

        // se ultrapassar o número de vezes de algum lado
        if(ladoD > 10){
            int[] ladoEsquerdo = {1,3,5,7}; // posições onde a img certa é no lado esquerdo
            desafioDaVez = ladoEsquerdo[numeroAleatorio(0,3)];
            if(desafios1 > 10)
                desafioDaVez = ladoEsquerdo[numeroAleatorio(2,3)];// pegar dasafios1 do vetor
            else if(desafios2 > 10)
                desafioDaVez = ladoEsquerdo[numeroAleatorio(0,1)];
        }else if(ladoE > 10){
            int[] ladoDireito = {0,2,4,6}; // posições onde a img certa é no lado direito
            desafioDaVez = ladoDireito[numeroAleatorio(0,3)];
            if(desafios1 > 10)
                desafioDaVez = ladoDireito[numeroAleatorio(2,3)];
            else if(desafios2 > 10)
                desafioDaVez = ladoDireito[numeroAleatorio(0,1)];
        }

        desafioAtual = desafios.get(desafioDaVez);
    }

    private static void l1() {
        String desafioAtual2;
        int desafioDaVez = numeroAleatorio(0,3);// pegar um desafio aleatório

        if(desafioDaVez == 0 || desafioDaVez == 1){// se for um dos 2 primeiros é o simbolo 2
            desafioAtual2 = "simbolo1";
            Log.i("DEBUGL1", "AGORA É ESTRELA ");
        }else {// se for um dos 2 ultimos é o simbolo 2
            Log.i("DEBUGL1", "AGORA É CIRCULO ");
            desafioAtual2 = "simbolo2";
        }

        if(desafioAtual2.equals(desafioAnterior)){ // se o desafio atualo for igual o desafio anterior
            numRepeticoes++;
            Log.i("DEBUGL1", "REPETIU " + numRepeticoes +" VEZES");
        }else {// se não for igual o número de repetições zera
            Log.i("DEBUGL1", "ZEROUUU ");
            numRepeticoes = 0;
        }

        if(numRepeticoes >= 3){// se o número de repeticoes chegar a 3 deve-se saber qual simbolo se repetiu
            if(desafioAnterior.equals("simbolo1")){// se for o simbolo 1 na a próxima vez força o simbolo 2
                desafioDaVez = numeroAleatorio(2,3);
                Log.i("DEBUGL1", "FORÇOU CIRCULO ");
            }else{ // se não força o simbolo 1
                desafioDaVez = numeroAleatorio(0,1);
                Log.i("DEBUGL1", "FORÇOU TRIANGULO");
            }
            numRepeticoes = 0;
            Log.i("DEBUGL1", "ZEROUUU ");
        }

        if(desafioDaVez == 0 || desafioDaVez == 1){ // o desafio anterior sempre é declarado no final da interação
            desafioAnterior = "simbolo1";
        }else {
            desafioAnterior = "simbolo2";
        }

        desafioAtual = desafios.get(desafioDaVez);
    }

    //retorna um numero de 0 até o tamanho da lista
    private static int numeroAleatorio(int min, int max) {
        Random random = new Random(); // gerar número aleatório
        int numeroTmp = random.nextInt(max - (min - 1)) + min;
        return numeroTmp;
    }

    /*
     Erros que este método pode acarretar:
     1 - Se o servidor começar antes de algum cliente, este respectivo cliente terá um erro
     2 - Se houver menos que dois clientes conectados ao mestre
    */
    public static void comecarInteracao() {
        sortearDesafio();

        enviarParaEscravos(desafioAtual.getImg1(), desafioAtual.getImg2());

        mudarImagem(desafioAtual.getImgCorreta());
    }

    // Enviar comandos para enviar para escravos 1 e 2
    private static void enviarParaEscravos(int img1, int img2) {
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
