package com.example.bolsista.novatentativa.sockets;

import android.content.Context;
import android.util.Log;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.arquitetura.Servidor;
import com.example.bolsista.novatentativa.modelo.Desafio;
import com.example.bolsista.novatentativa.modelo.Ensaio;
import com.example.bolsista.novatentativa.viewsModels.TesteViewModel;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class AleatorioTeste extends PreTeste {
    private static Desafio desafioAtual;
    private static ArrayList<Desafio> desafios = new ArrayList<>();

    public AleatorioTeste(Socket cliente, int numCliente, Context context) {
        super(cliente, numCliente, context);
        preecherDesafios();
    }

    private void preecherDesafios() {
        desafios = new ArrayList<>();
        desafios.addAll(TesteViewModel.teste.getValue().getDesafios());
    }

    @Override
    public void tratarConexao() throws IOException, ClassNotFoundException {
        int numRodadas = Objects.requireNonNull(TesteViewModel.teste.getValue()).getQtdQuestoesPorSessao();

        while (rodada <= numRodadas) {
            Ensaio ensaio = new Ensaio(); // Iniciando um ensaio
            ensaio.setId(rodada + "");
            msg = leitor.readLine().split("-");
            ensaio.setIdDesafio(Integer.toString(rodada));

            int comando = Integer.parseInt(msg[1]);

            if (comando == desafioAtual.getImgCorreta()) {
                terminarContagemTempo();
                jogar.tocarAcerto();
                rodada++;
                esp32(ABRIR_MOTOR);
                ensaio.setAcerto(true);
                ensaio.setDesafio(desafioAtual);
                ensaio.setTempoAcerto(calcularTempoQuePassou());
                esperar();
                if (!Servidor.controleRemoto && rodada <= numRodadas) {
                    dormir(TesteViewModel.teste.getValue().getIntervalo1()); // tempo de espera do mestre
                    novaInteracao(); //fazer nova interação de imagens entre os tablets
                }
            } else if (comando == TROCAR_IMAGENS) {//trocar imagens;
                novaInteracao();
                continue;
            } else if (comando == FECHAR_SOCKET) {//fechar socket
                desconectarControle();
                continue;
            } else { //O cavalo errou
                jogar.tocarError();
                ensaio.setAcerto(false);
                Log.i("DESAFIOATUAL", " IMG1 = " + desafioAtual.getImg1() + " IMG2 = " +desafioAtual.getImg2());
                ensaio.setDesafio(desafioAtual);
            }
            TesteViewModel.sessao.getEnsaios().add(ensaio);

        }
        terminar();
        jogar.terminar();
        TesteViewModel.adicionarNovaSessao();
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

    private void novaInteracao() {
        esp32(FECHAR_MOTOR);//enviar comando para o servo fechar no esp32

        sortearDesafio();

        mudarImagem(desafioAtual.getImgCorreta());
        dormir(TesteViewModel.teste.getValue().getIntervalo2());

        enviarParaEscravos(desafioAtual.getImg1(), desafioAtual.getImg2());
    }

    private static void sortearDesafio(){
        desafioAtual = desafios.get(numeroAleatorio(0, desafios.size() - 1));

        int imgAleatoria = desafios.get(numeroAleatorio(0, desafios.size() - 1)).getImgCorreta();

        Integer lado = numeroAleatorio(0,1);

        if(lado.equals(0)){
            desafioAtual.setImg1(desafioAtual.getImgCorreta());
            desafioAtual.setImg2(imgAleatoria);
        }else {
            desafioAtual.setImg1(imgAleatoria);
            desafioAtual.setImg2(desafioAtual.getImgCorreta());
        }
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
