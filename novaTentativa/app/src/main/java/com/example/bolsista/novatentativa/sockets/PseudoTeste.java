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
    private static String simboloAnterior = "";
    private static int numRepeticoes = 0;

    // testes com 8 desafios
    private static int familia1 = 0, familia2 = 0, ladoE = 0, ladoD = 0;

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
        int testeId = Integer.parseInt(TesteViewModel.teste.getValue().getId());
        switch (testeId){
            case 1:
                l11();
                break;
            case 2:
                l22();
                break;
            case 3:
                ///
                break;
            case 4:
                ///
                break;
            case 5:
                ///
                break;
            default:
                l11();
        }
    }

    private static void l11(){
        String simboloAtual;
        int desafioDaVez = numeroAleatorio(0, desafios.size() - 1);// pegar um desafio aleatório

        // Identificar o simbolo atual
        if(desafioDaVez == 0 || desafioDaVez == 1){// se for um dos 2 primeiros é o simbolo 1
            simboloAtual = "simbolo1";
        }else {// se for um dos 2 ultimos é o simbolo 2
            simboloAtual = "simbolo2";
        }

        // Incrementa se o símbolo atual for igual o símbolo anterior, se não, zera
        if(simboloAtual.equals(simboloAnterior)){
            numRepeticoes++;
        }else {// se não for igual o número de repetições zera
            numRepeticoes = 0;
        }

        // Se o número de repeticoes chegar a 3 deve-se saber qual símbolo se repetiu e forçar o outro
        if(numRepeticoes >= 3){
            if(simboloAnterior.equals("simbolo1")){// Se for o símbolo 1, força o símbolo 2
                desafioDaVez = numeroAleatorio(2,3);
            }else{ // Se não força o símbolo 1
                desafioDaVez = numeroAleatorio(0,1);
            }
        }

        // Identificar qual lado foi mostrado
        if(desafioDaVez == 0 || desafioDaVez == 2)// Posicão onde o lado direito é correto
            ladoD++;
        else
            ladoE++;

        Log.i("TESTEL1_Lados", "Lado direito contando com "+ ladoD+ " vezes");
        Log.i("TESTEL1_Lados", "Lado esquerdo contando com "+ ladoE+ " vezes");

        // Se ultrapassar o número de vezes de algum lado
        if(ladoD > 10){
            int[] ladoEsquerdo = {1,3}; // posições onde a img certa é no lado esquerdo
            desafioDaVez = ladoEsquerdo[numeroAleatorio(0,1)];

            Log.i("TESTEL1_Lados", "Lado DIREITO atingiu limite");

            if(numRepeticoes >= 3){
                if(simboloAnterior.equals("simbolo1")){// Se for o símbolo 1, força o símbolo 2
                    desafioDaVez = ladoEsquerdo[1];
                }else{ // Se não força o símbolo 1
                    desafioDaVez = ladoEsquerdo[0];
                }
            }

        }else if(ladoE > 10){
            int[] ladoDireito = {0,2}; // posições onde a img certa é no lado direito
            desafioDaVez = ladoDireito[numeroAleatorio(0,1)];

            Log.i("TESTEL1_Lados", "Lado ESQUERDO atingiu limite");

            if(numRepeticoes >= 3){
                if(simboloAnterior.equals("simbolo1")){// Se for o símbolo 1, força o símbolo 2
                    desafioDaVez = ladoDireito[1];
                }else{ // Se não força o símbolo 1
                    desafioDaVez = ladoDireito[0];
                }
            }
        }

        // Por último, fala quem foi o símbolo escolhido para o desafio
        if(desafioDaVez == 0 || desafioDaVez == 1){
            simboloAnterior = "simbolo1";
        }else {
            simboloAnterior = "simbolo2";
        }

        // Zera número de repetições se chegar a 3, pois já foi tratada
        if(numRepeticoes >= 3)
            numRepeticoes = 0;

        desafioAtual = desafios.get(desafioDaVez);
    }

    private static void l22(){
        String simboloAtual;
        int desafioDaVez = numeroAleatorio(0, desafios.size() - 1);// pegar um desafio aleatório

        // Verificar qual família foi escolhida
        if(desafioDaVez < 4){
            familia1++;
        }else{
            familia2++;
        }

        // Se alguma família chegar a 10, somente a outra será chamada
        if(familia1 > 10)
            desafioDaVez = numeroAleatorio(4, desafios.size() - 1);
        else if (familia2 > 10)
            desafioDaVez = numeroAleatorio(0,3);

        // Identificar qual lado foi mostrado
        if(desafioDaVez == 0 || desafioDaVez == 2 || desafioDaVez == 4 || desafioDaVez == 6)// posicoes onde o lado direito é correto
            ladoD++;
        else
            ladoE++;

        Log.i("TESTEL2_Lados", "Lado direito contando com "+ ladoD+ " vezes");
        Log.i("TESTEL2_Lados", "Lado esquerdo contando com "+ ladoE+ " vezes");

        // Se ultrapassar o número de vezes de algum lado
        if(ladoD > 10){
            Log.i("TESTEL2_Lados", "Lado DIREITO atingiu limite");

            int[] ladoEsquerdo = {1,3,5,7}; // posições onde a img certa é no lado esquerdo
            desafioDaVez = ladoEsquerdo[numeroAleatorio(0,3)];
            if(familia1 > 10)// verifica novamente se um desafio ultrapassou 10 vezes
                desafioDaVez = ladoEsquerdo[numeroAleatorio(2,3)];// pegar dasafios1 do vetor
            else if(familia2 > 10)
                desafioDaVez = ladoEsquerdo[numeroAleatorio(0,1)];
        }else if(ladoE > 10){
            Log.i("TESTEL2_Lados", "Lado ESQUERDO atingiu limite");

            int[] ladoDireito = {0,2,4,6}; // posições onde a img certa é no lado direito
            desafioDaVez = ladoDireito[numeroAleatorio(0,3)];
            if(familia1 > 10)
                desafioDaVez = ladoDireito[numeroAleatorio(2,3)];
            else if(familia2 > 10)
                desafioDaVez = ladoDireito[numeroAleatorio(0,1)];
        }

        // ------------------- TRATAR SIMBOLOS SEM QUEBRAR AS OUTRAS REGRAS ------------------------
        // Se o número de repeticoes chegar a 3 deve-se saber qual símbolo se repetiu e forçar o outro
        if(numRepeticoes >= 3){
            if(simboloAnterior.equals("simbolo1")){// Se for o símbolo 1, força o símbolo 2
                int[] simbolo2 = {2,3,6,7}; // posições onde o simbolo 2 está presente no meste
                desafioDaVez = simbolo2[numeroAleatorio(0,4)];

                // Se ultrapassar o número de vezes de algum lado
                if(ladoD > 10){
                    int[] ladoEsquerdo = {3,7}; // posições onde a img certa é no lado esquerdo
                    desafioDaVez = ladoEsquerdo[numeroAleatorio(0,1)];
                    if(familia1 > 10)// verifica novamente se um desafio ultrapassou 10 vezes
                        desafioDaVez = ladoEsquerdo[1];// pegar dasafios1 do vetor
                    else if(familia2 > 10)
                        desafioDaVez = ladoEsquerdo[0];
                }else if(ladoE > 10){
                    int[] ladoDireito = {2,6}; // posições onde a img certa é no lado direito
                    desafioDaVez = ladoDireito[numeroAleatorio(0,1)];
                    if(familia1 > 10)
                        desafioDaVez = ladoDireito[1];
                    else if(familia2 > 10)
                        desafioDaVez = ladoDireito[0];
                }
            }else{ // Se não força o símbolo 1
                int[] simbolo1 = {0,1,4,5}; // posições onde o simbolo 2 está presente no mestre
                desafioDaVez = simbolo1[numeroAleatorio(0,4)];

                // Se ultrapassar o número de vezes de algum lado
                if(ladoD > 10){
                    int[] ladoEsquerdo = {1,5}; // posições onde a img certa é no lado esquerdo
                    desafioDaVez = ladoEsquerdo[numeroAleatorio(0,1)];
                    if(familia1 > 10)// verifica novamente se um desafio ultrapassou 10 vezes
                        desafioDaVez = ladoEsquerdo[1];// pegar dasafios1 do vetor
                    else if(familia2 > 10)
                        desafioDaVez = ladoEsquerdo[0];
                }else if(ladoE > 10){
                    int[] ladoDireito = {0,4}; // posições onde a img certa é no lado direito
                    desafioDaVez = ladoDireito[numeroAleatorio(0,1)];
                    if(familia1 > 10)
                        desafioDaVez = ladoDireito[1];
                    else if(familia2 > 10)
                        desafioDaVez = ladoDireito[0];
                }
            }
        }

        Log.i("TESTEL2_Familias", "Primeira família foi selecionada "+ familia1+ " vezes");
        Log.i("TESTEL2_Familias", "Segunda família foi selecionada "+ familia2+ " vezes");

        // Identificar o simbolo atual
        if(desafioDaVez == 0 || desafioDaVez == 1 || desafioDaVez == 4 || desafioDaVez == 5){// se for um desses é o simbolo 1
            simboloAtual = "simbolo1";
        }else {// se for um dos 2 ultimos é o simbolo 2
            simboloAtual = "simbolo2";
        }

        // Incrementa se o símbolo atual for igual o símbolo anterior, se não, zera
        if(simboloAtual.equals(simboloAnterior)){
            numRepeticoes++;
        }else {// se não for igual o número de repetições zera
            numRepeticoes = 0;
        }
        // Zera número de repetições se chegar a 3, pois já foi tratada
        if(numRepeticoes >= 3)
            numRepeticoes = 0;

        desafioAtual = desafios.get(desafioDaVez);
    }

    // desafio 1 e desafio 2 devem aparecer 10 vezes cada no tablet mestre
    // as opções corretas na esquerda e direita são igualmente distribuídas
    private static void l2() {
        int desafioDaVez;

        desafioDaVez = numeroAleatorio(0, desafios.size() - 1); // pega um desafio aleatório de 0 a 7

        if(desafioDaVez < 4){ // se for menor que 4 desafios1 é incrementado
            familia1++;
            Log.i("DEBUGL2", "primeira família apareceram " + familia1 +" vezes");
            if(desafioDaVez == 0 || desafioDaVez == 2)// posicoes onde o lado direito é correto
                ladoD++;
            else
                ladoE++;
        }else{// se não, desafios2 é incrementado
            familia2++;
            Log.i("DEBUGL2", "segunda família apareceram " + familia2 +" vezes");
            if(desafioDaVez == 4 || desafioDaVez == 6)// posicoes onde o lado direito é correto
                ladoD++;
            else
                ladoE++;
        }

        // se algum destes chegar a 10, somente o outro será chamado
        if(familia1 > 10){
            Log.i("DEBUGL22", "Primeira família ultrapassaram 10, agora somente desafios 2");
            desafioDaVez = numeroAleatorio(4, desafios.size() - 1);
        }else if (familia2 > 10){
            desafioDaVez = numeroAleatorio(0,3);
            Log.i("DEBUGL22", "Segunda família ultrapassaram 10, agora somente desafios 1");
        }

        // se ultrapassar o número de vezes de algum lado
        if(ladoD > 10){
            int[] ladoEsquerdo = {1,3,5,7}; // posições onde a img certa é no lado esquerdo
            desafioDaVez = ladoEsquerdo[numeroAleatorio(0,3)];
            if(familia1 > 10)// verifica novamente se um desafio ultrapassou 10 vezes
                desafioDaVez = ladoEsquerdo[numeroAleatorio(2,3)];// pegar dasafios1 do vetor
            else if(familia2 > 10)
                desafioDaVez = ladoEsquerdo[numeroAleatorio(0,1)];
        }else if(ladoE > 10){
            int[] ladoDireito = {0,2,4,6}; // posições onde a img certa é no lado direito
            desafioDaVez = ladoDireito[numeroAleatorio(0,3)];
            if(familia1 > 10)
                desafioDaVez = ladoDireito[numeroAleatorio(2,3)];
            else if(familia2 > 10)
                desafioDaVez = ladoDireito[numeroAleatorio(0,1)];
        }

        desafioAtual = desafios.get(desafioDaVez);
    }


    // não deixar um simbolo se repetir mais do que 3 vezes consecutivas no tablet mestre
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

        if(desafioAtual2.equals(simboloAnterior)){ // se o desafio atual for igual o desafio anterior
            numRepeticoes++;
            Log.i("DEBUGL1", "REPETIU " + numRepeticoes +" VEZES");
        }else {// se não for igual o número de repetições zera
            Log.i("DEBUGL1", "ZEROUUU ");
            numRepeticoes = 0;
        }

        if(numRepeticoes >= 3){// se o número de repeticoes chegar a 3 deve-se saber qual simbolo se repetiu
            if(simboloAnterior.equals("simbolo1")){// se for o simbolo 1 na a próxima vez força o simbolo 2
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
            simboloAnterior = "simbolo1";
        }else {
            simboloAnterior = "simbolo2";
        }

        desafioAtual = desafios.get(desafioDaVez);
    }

    // retorna um numero aleatório do min ao max
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
