package com.example.bolsista.novatentativa.configuracao;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bolsista.novatentativa.MainActivity;
import com.example.bolsista.novatentativa.R;

public class ConfigurarTeste extends AppCompatActivity {
    EditText qtdQuestao, intervaloQuestoes, intervalo2;
    ImageView info1, info2,info3;
    TextView info1TextView, info2TextView,info3TextView;
    Button somErro1, somErro2, somErro3, somAcerto1, somAcerto2, somAcerto3;
    CheckBox formas, preenchimento, tamanho;
    Button irPara;

    private MediaPlayer mp;

    private int erroEscolhido;
    private int acertoEscolhido;

    public static Configuracao configuracao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_teste);

        inicializar();
        listener();

    }

    private void listener() {
        info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info1TextView.getVisibility() == View.GONE){
                    info1TextView.setVisibility(View.VISIBLE);
                }else{
                    info1TextView.setVisibility(View.GONE);
                }
            }
        });

        info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info2TextView.getVisibility() == View.GONE){
                    info2TextView.setVisibility(View.VISIBLE);
                }else{
                    info2TextView.setVisibility(View.GONE);
                }
            }
        });

        info3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info3TextView.getVisibility() == View.GONE){
                    info3TextView.setVisibility(View.VISIBLE);
                }else{
                    info3TextView.setVisibility(View.GONE);
                }
            }
        });

        //Sons

        somErro1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = MediaPlayer.create(ConfigurarTeste.this, R.raw.error);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                });
                mp.start();

                erroEscolhido = R.raw.error;
            }
        });

        somErro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = MediaPlayer.create(ConfigurarTeste.this, R.raw.error2);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                });
                mp.start();

                erroEscolhido = R.raw.error2;
            }
        });

        somErro3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = MediaPlayer.create(ConfigurarTeste.this, R.raw.error3);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                });
                mp.start();

                erroEscolhido = R.raw.error3;
            }
        });

        somAcerto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = MediaPlayer.create(ConfigurarTeste.this, R.raw.sucess);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                });
                mp.start();

                acertoEscolhido = R.raw.sucess;
            }
        });

        somAcerto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = MediaPlayer.create(ConfigurarTeste.this, R.raw.sucess2);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                });
                mp.start();

                acertoEscolhido = R.raw.sucess2;
            }
        });

        somAcerto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = MediaPlayer.create(ConfigurarTeste.this, R.raw.sucess3);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                });
                mp.start();

                acertoEscolhido = R.raw.sucess3;
            }
        });

        irPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazerConfiguracao();
                Intent i = new Intent(ConfigurarTeste.this,MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void fazerConfiguracao(){
        configuracao = new Configuracao();
        configuracao.setQtdQuestoes(Integer.parseInt(qtdQuestao.getText().toString()));
        configuracao.setIntervalo1(Integer.parseInt(intervaloQuestoes.getText().toString()));
        configuracao.setIntervalo2(Integer.parseInt(intervalo2.getText().toString()));
        configuracao.setSomErro(erroEscolhido);
        configuracao.setSomAcerto(acertoEscolhido);
        configuracao.setImagens(getImagens());

    }

    private int[] getImagens(){
        if(formas.isChecked() && preenchimento.isChecked()){
            int imagens[] = {R.drawable.circulo, R.drawable.triangulo,R.drawable.coracao, R.drawable.estrela2,
                    R.drawable.estrela,R.drawable.retangulo, R.drawable.b_circulo,R.drawable.b_losango,
                    R.drawable.b_coracao, R.drawable.b_hexagono,R.drawable.b_retangulo,R.drawable.b_estrela2};
            return imagens;
        }else{
            int imagens[] = {R.drawable.circulo, R.drawable.triangulo,R.drawable.coracao, R.drawable.estrela2,
                    R.drawable.estrela,R.drawable.retangulo};
            return imagens;
        }
    }

    private void inicializar() {
        qtdQuestao = findViewById(R.id.qtdQuestao);
        intervaloQuestoes = findViewById(R.id.intervaloQuestoes);
        intervalo2 = findViewById(R.id.intervalo2);

        info1 = findViewById(R.id.info1);
        info2 = findViewById(R.id.info2);
        info3 = findViewById(R.id.info3);

        info1TextView = findViewById(R.id.info1TextView);
        info2TextView = findViewById(R.id.info2TextView);
        info3TextView = findViewById(R.id.info3TextView);

        somErro1 = findViewById(R.id.somErro1);
        somErro2 = findViewById(R.id.somErro2);
        somErro3 = findViewById(R.id.somErro3);
        somAcerto1 = findViewById(R.id.somAcerto1);
        somAcerto2 = findViewById(R.id.somAcerto2);
        somAcerto3 = findViewById(R.id.somAcerto3);

        formas = findViewById(R.id.formasBtn);
        preenchimento = findViewById(R.id.preenchimentoBtn);
        tamanho = findViewById(R.id.tamanhoBtn);

        irPara = findViewById(R.id.irPara);
    }
}
