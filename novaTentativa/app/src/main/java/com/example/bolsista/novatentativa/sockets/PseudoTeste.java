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
import com.google.android.gms.common.util.JsonUtils;

import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class PseudoTeste extends PreTeste {
    private static Desafio desafioAtual;
    private static ArrayList<Desafio> desafios = new ArrayList<>();

    // Variáveis para controle dos testes
    private static String simboloAnterior = "";
    private static int numRepeticoes = 0;
    private static int simbolo1 = 0, simbolo2 = 0;
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
        int numRodadas = Objects.requireNonNull(TesteViewModel.teste.getValue()).getQtdEnsaiosPorSessao(); // Quantidade de questões

        while (rodada <= numRodadas) {
            Ensaio ensaio = new Ensaio(); // Iniciando um ensaio
            ensaio.setId(rodada + "");
            msg = leitor.readLine().split("-");
            ensaio.setIdDesafio(Integer.toString(rodada));
            ensaio.setDesafio(desafioAtual);

            int comando = Integer.parseInt(msg[1]);

            if (comando == desafioAtual.getImgCorreta()) {
                terminarContagemTempo();
                jogar.tocarAcerto();
                rodada++;
                esp32(ABRIR_MOTOR);
                ensaio.setAcerto(true);
                ensaio.setTempoAcerto(calcularTempoQuePassou());
                esperar();
                if (!Servidor.controleRemoto && rodada <= numRodadas) {
                    dormir(TesteViewModel.teste.getValue().getIntervalo1()); // tempo de espera do mestre
                    novaInteracao(); //fazer nova interação de imagens entre os tablets
                }
            } else if (comando == TROCAR_IMAGENS) {//trocar imagens
                ensaio.setAcerto(true);
                novaInteracao();
                continue;
            } else if (comando == FECHAR_SOCKET) {//fechar socket
                desconectarControle();
            } else { //O cavalo errou
                jogar.tocarError();
                ensaio.setAcerto(false);
            }
            TesteViewModel.sessao.getEnsaios().add(ensaio);
            Log.i("TESTE_Simbolos", "Simbolo se repetiu "+ numRepeticoes+ " vezes");
        }
        terminar();
        jogar.terminar();
        TesteViewModel.adicionarNovaSessao();
    }

    public static void clicouNoMestre(){
        Ensaio ensaio = new Ensaio(); // Iniciando um ensaio
        ensaio.setId(rodada + "");
        ensaio.setIdDesafio(Integer.toString(rodada));
        ensaio.setDesafio(desafioAtual);

        ensaio.setAcerto(false);
        jogar.tocarError();
        TesteViewModel.sessao.getEnsaios().add(ensaio);

    }

    private void novaInteracao() {
        esp32(FECHAR_MOTOR);//enviar comando para o servo fechar no esp32

        sortearDesafio();

        mudarImagem(desafioAtual.getImgCorreta());
        dormir(TesteViewModel.teste.getValue().getIntervalo2());

        enviarParaEscravos(desafioAtual.getImg1(), desafioAtual.getImg2());
    }

    //mudar imagens para preto, e espera um novo sorteio
    private void esperar() throws IOException {
        mudarImagem(999);

        for (int i = 0; i < clientes.size(); i++) {
            PreTeste destino = clientes.get(i);
            destino.getEscritor().println(999);
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
                l1();
                break;
            case 2:
                l2();
                break;
            case 3:
                l3();
                break;
            case 4:
                l2();
                break;
            case 5:
                t2();
                break;
            default:
                l1();
        }
    }

    private static void l1(){
        int[] interacoesL1 = {0, 2, 3, 2, 1, 3, 2, 0, 1, 0, 3, 0, 2, 1, 0, 3, 2, 1 ,3, 1};

        desafioAtual = desafios.get(interacoesL1[rodada-1]);
    }

    private static void l3(){
        String simboloAtual;
        int desafioDaVez = numeroAleatorio(0, desafios.size() - 1);// pegar um desafio aleatório

        // Identificar o simbolo atual
        if(desafioDaVez == 0 || desafioDaVez == 1){// se for um dos 2 primeiros é o simbolo 1
            simboloAtual = "simbolo1";
            simbolo1++;
        }else {// se for um dos 2 ultimos é o simbolo 2
            simboloAtual = "simbolo2";
            simbolo2++;
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

        // DISTRIBUIÇÃO

        // Se ultrapassar o número de vezes de algum lado
        if(ladoD >= 10){
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

        }else if(ladoE >= 10){
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

        // Identificar qual lado foi mostrado
        if(desafioDaVez == 0 || desafioDaVez == 2)// Posicão onde o lado direito é correto
            ladoD++;
        else
            ladoE++;

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

    private static void l2(){
        String simboloAtual;
        int desafioDaVez = numeroAleatorio(0, desafios.size() - 1);// pegar um desafio aleatório

        // Se alguma família chegar a 10, somente a outra será chamada
        if(familia1 >= 10)
            desafioDaVez = numeroAleatorio(4, desafios.size() - 1);
        else if (familia2 >= 10)
            desafioDaVez = numeroAleatorio(0,3);

        // Se ultrapassar o número de vezes de algum lado
        if(ladoD >= 10){
            Log.i("TESTEL2_Lados", "Lado DIREITO atingiu limite");

            int[] ladoEsquerdo = {1,3,5,7}; // posições onde a img certa é no lado esquerdo
            desafioDaVez = ladoEsquerdo[numeroAleatorio(0,3)];
            if(familia1 >= 10)// verifica novamente se um desafio ultrapassou 10 vezes
                desafioDaVez = ladoEsquerdo[numeroAleatorio(2,3)];// pegar dasafios1 do
            else if(familia2 >= 10)
                desafioDaVez = ladoEsquerdo[numeroAleatorio(0,1)];
        }else if(ladoE >= 10){
            Log.i("TESTEL2_Lados", "Lado ESQUERDO atingiu limite");

            int[] ladoDireito = {0,2,4,6}; // posições onde a img certa é no lado direito
            desafioDaVez = ladoDireito[numeroAleatorio(0,3)];
            if(familia1 >= 10)
                desafioDaVez = ladoDireito[numeroAleatorio(2,3)];
            else if(familia2 >= 10)
                desafioDaVez = ladoDireito[numeroAleatorio(0,1)];
        }

        // ------------------- TRATAR SIMBOLOS SEM QUEBRAR AS OUTRAS REGRAS ------------------------
        // Se o número de repeticoes chegar a 3 deve-se saber qual símbolo se repetiu e forçar o outro
        if(numRepeticoes >= 2){
            if(simboloAnterior.equals("simbolo1")){// Se for o símbolo 1, força o símbolo 2
                int[] simbolo2 = {2,3,6,7}; // posições onde o simbolo 2 está presente no meste
                desafioDaVez = simbolo2[numeroAleatorio(0,3)];

                // Verifica familias
                if(familia1 >= 10)// verifica novamente se um desafio ultrapassou 10 vezes
                    desafioDaVez = simbolo2[numeroAleatorio(2,3)];// pegar familia2 do vetor
                else if(familia2 >= 10)
                    desafioDaVez = simbolo2[numeroAleatorio(0,1)];// pegar familia1 do vetor

                // Se ultrapassar o número de vezes de algum lado
                if(ladoD >= 10){
                    int[] ladoEsquerdo = {3,7}; // posições onde a img certa é no lado esquerdo
                    desafioDaVez = ladoEsquerdo[numeroAleatorio(0,1)];
                    if(familia1 >= 10)// verifica novamente se um desafio ultrapassou 10 vezes
                        desafioDaVez = ladoEsquerdo[1];// pegar dasafios1 do vetor
                    else if(familia2 >= 10)
                        desafioDaVez = ladoEsquerdo[0];
                }else if(ladoE >= 10){
                    int[] ladoDireito = {2,6}; // posições onde a img certa é no lado direito
                    desafioDaVez = ladoDireito[numeroAleatorio(0,1)];
                    if(familia1 >= 10)
                        desafioDaVez = ladoDireito[1];
                    else if(familia2 >= 10)
                        desafioDaVez = ladoDireito[0];
                }

            }else{ // Se não força o símbolo 1
                int[] simbolo1 = {0,1,4,5}; // posições onde o simbolo 2 está presente no mestre
                desafioDaVez = simbolo1[numeroAleatorio(0,3)];

                // Verifica familias
                if(familia1 >= 10)// verifica novamente se um desafio ultrapassou 10 vezes
                    desafioDaVez = simbolo1[numeroAleatorio(2,3)];// pegar familia2 do vetor
                else if(familia2 >= 10)
                    desafioDaVez = simbolo1[numeroAleatorio(0,1)];// pegar familia1 do vetor

                // Se ultrapassar o número de vezes de algum lado
                if(ladoD >= 10){
                    int[] ladoEsquerdo = {1,5}; // posições onde a img certa é no lado esquerdo
                    desafioDaVez = ladoEsquerdo[numeroAleatorio(0,1)];
                    if(familia1 >= 10)// verifica novamente se um desafio ultrapassou 10 vezes
                        desafioDaVez = ladoEsquerdo[1];// pegar dasafios1 do vetor
                    else if(familia2 >= 10)
                        desafioDaVez = ladoEsquerdo[0];
                }else if(ladoE >= 10){
                    int[] ladoDireito = {0,4}; // posições onde a img certa é no lado direito
                    desafioDaVez = ladoDireito[numeroAleatorio(0,1)];
                    if(familia1 >= 10)
                        desafioDaVez = ladoDireito[1];
                    else if(familia2 >= 10)
                        desafioDaVez = ladoDireito[0];
                }
            }
            numRepeticoes = 0;
        }

        // Verificar qual família foi escolhida
        if(desafioDaVez < 4){
            familia1++;
        }else{
            familia2++;
        }

        // Identificar o simbolo atual
        if(desafioDaVez == 0 || desafioDaVez == 1 || desafioDaVez == 4 || desafioDaVez == 5){// se for um desses é o simbolo 1
            simboloAtual = "simbolo1";
        }else {// se for um dos 2 ultimos é o simbolo 2
            simboloAtual = "simbolo2";
        }

        // Identificar qual lado foi mostrado
        if(desafioDaVez == 0 || desafioDaVez == 2 || desafioDaVez == 4 || desafioDaVez == 6)// posicoes onde o lado direito é correto
            ladoD++;
        else
            ladoE++;

        // Incrementa se o símbolo atual for igual o símbolo anterior, se não, zera
        if(simboloAtual.equals(simboloAnterior)){
            numRepeticoes++;
        }else {// se não for igual o número de repetições zera
            numRepeticoes = 0;
        }

        // Por último, fala quem foi o símbolo escolhido para o desafio
        if(desafioDaVez == 0 || desafioDaVez == 1 || desafioDaVez == 4 || desafioDaVez == 5){
            simboloAnterior = "simbolo1";
        }else {
            simboloAnterior = "simbolo2";
        }

        Log.i("TESTE_Lados", "Lado direito contando com "+ ladoD+ " vezes");
        Log.i("TESTE_Lados", "Lado esquerdo contando com "+ ladoE+ " vezes");
        Log.i("TESTE_Familias", "Primeira família foi selecionada "+ familia1+ " vezes");
        Log.i("TESTE_Familias", "Segunda família foi selecionada "+ familia2+ " vezes");

        desafioAtual = desafios.get(desafioDaVez);
    }

    private static void t2() {
        String simboloAtual;
        int desafioDaVez = numeroAleatorio(0, desafios.size() - 1);// pegar um desafio aleatório

        // Se ultrapassar o número de vezes de algum lado
        if(ladoD >= 10){
            Log.i("TESTEL2_Lados", "Lado DIREITO atingiu limite");

            int[] ladoEsquerdo = {1,3,5,7}; // posições onde a img certa é no lado esquerdo
            desafioDaVez = ladoEsquerdo[numeroAleatorio(0,3)];
        }else if(ladoE >= 10){
            Log.i("TESTEL2_Lados", "Lado ESQUERDO atingiu limite");

            int[] ladoDireito = {0,2,4,6}; // posições onde a img certa é no lado direito
            desafioDaVez = ladoDireito[numeroAleatorio(0,3)];
        }

        // ------------------- TRATAR SIMBOLOS SEM QUEBRAR AS OUTRAS REGRAS ------------------------
        // Se o número de repeticoes chegar a 3 deve-se saber qual símbolo se repetiu e forçar o outro
        if(numRepeticoes >= 2){
            if(desafioDaVez <= 5){
                desafioDaVez = desafioDaVez + 2; // pula dois desafios a frente para resolver a repetição
                int desafioAux = desafioDaVez;// precisa-se guardar este valor para comparação

                // Se ultrapassar o número de vezes de algum lado
                if(ladoD >= 10){
                    int[] ladoEsquerdo = {1,3,5,7}; // posições onde a img certa é no lado esquerdo
                    int ladoAux = numeroAleatorio(0,3);// precisa-se guardar este valor para comparação

                    desafioDaVez = ladoEsquerdo[ladoAux];

                    if(desafioDaVez == desafioAux){// se for igual a o escolhido no primeiro if
                        if (ladoAux == 0){
                            desafioDaVez = ladoEsquerdo[1];
                        }else if(ladoAux == 1){
                            desafioDaVez = ladoEsquerdo[2];
                        }else if(ladoAux == 2){
                            desafioDaVez = ladoEsquerdo[3];
                        }else{
                            desafioDaVez = ladoEsquerdo[0];
                        }
                    }

                }else if(ladoE >= 10){
                    int[] ladoDireito = {0,2,4,6}; // posições onde a img certa é no lado direito
                    int ladoAux = numeroAleatorio(0,3);// precisa-se guardar este valor para comparação

                    desafioDaVez = ladoDireito[ladoAux];

                    if(desafioDaVez == desafioAux){// se for igual a o escolhido no primeiro if
                        if (ladoAux == 0){
                            desafioDaVez = ladoDireito[1];
                        }else if(ladoAux == 1){
                            desafioDaVez = ladoDireito[2];
                        }else if(ladoAux == 2){
                            desafioDaVez = ladoDireito[3];
                        }else{
                            desafioDaVez = ladoDireito[0];
                        }
                    }

                }
            }else{
                desafioDaVez = numeroAleatorio(0,5); // escolhe aleatóriamente um de trás
                int desafioAux = desafioDaVez;// precisa-se guardar este valor para comparação

                // Se ultrapassar o número de vezes de algum lado
                if(ladoD >= 10){
                    int[] ladoEsquerdo = {1,3,5}; // posições onde a img certa é no lado esquerdo
                    int ladoAux = numeroAleatorio(0,2);// precisa-se guardar este valor para comparação

                    desafioDaVez = ladoEsquerdo[ladoAux];

                    if(desafioDaVez == desafioAux){// se for igual a o escolhido no primeiro if
                        if (ladoAux == 0){
                            desafioDaVez = ladoEsquerdo[1];
                        }else if(ladoAux == 1){
                            desafioDaVez = ladoEsquerdo[2];
                        }else{
                            desafioDaVez = ladoEsquerdo[0];
                        }
                    }

                }else if(ladoE >= 10){
                    int[] ladoDireito = {0,2,4}; // posições onde a img certa é no lado direito
                    int ladoAux = numeroAleatorio(0,2);// precisa-se guardar este valor para comparação

                    desafioDaVez = ladoDireito[ladoAux];

                    if(desafioDaVez == desafioAux){// se for igual a o escolhido no primeiro if
                        if (ladoAux == 0){
                            desafioDaVez = ladoDireito[1];
                        }else if(ladoAux == 1){
                            desafioDaVez = ladoDireito[2];
                        }else{
                            desafioDaVez = ladoDireito[0];
                        }
                    }

                }
            }
        numRepeticoes = 0;
        }
        // Atribuir desafio para identificar
        desafioAtual = desafios.get(desafioDaVez);

        // Identificar o simbolo atual
        simboloAtual = Integer.toString(desafioAtual.getImgCorreta());

        // Identificar qual lado foi mostrado
        if(desafioDaVez == 0 || desafioDaVez == 2 || desafioDaVez == 4 || desafioDaVez == 6)// posicoes onde o lado direito é correto
            ladoD++;
        else
            ladoE++;

        // Incrementa se o símbolo atual for igual o símbolo anterior, se não, zera
        if(simboloAtual.equals(simboloAnterior)){
            numRepeticoes++;
        }else {// se não for igual o número de repetições zera
            numRepeticoes = 0;
        }

        // Por último, fala quem foi o símbolo escolhido para o desafio
        simboloAnterior = Integer.toString(desafioAtual.getImgCorreta());

        Log.i("TESTE_Lados", "Lado direito contando com "+ ladoD+ " vezes");
        Log.i("TESTE_Lados", "Lado esquerdo contando com "+ ladoE+ " vezes");
        Log.i("TESTE_REPETICOES", "Numero de repetições = "+ numRepeticoes);
    }

    /**
     * Retornar um número aleatório entre um valor máximo
     * e mínimo recebidos pelo parâmetro.
     *
     * @param min valor mínimo que pode ser retornado
     * @param max valor máximo que pode ser retornado
     * @return um número aleatório
     */
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
        PreTeste destino = clientes.get(0); // primeiro cliente conectado
        destino.getEscritor().println(img1);
        destino.getEscritor().flush();

        destino = clientes.get(1); // segundo cliente conectado
        destino.getEscritor().println(img2);
        destino.getEscritor().flush();

        iniciarContagemTempo();
    }
}
