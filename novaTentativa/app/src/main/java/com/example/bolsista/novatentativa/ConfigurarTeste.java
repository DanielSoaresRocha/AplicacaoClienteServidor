package com.example.bolsista.novatentativa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ConfigurarTeste extends AppCompatActivity {
    EditText qtdQuestao, intervaloQuestoes, intervalo2;
    ImageView info1, info2,info3;
    TextView info1TextView, info2TextView,info3TextView;
    Button irPara;

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

        irPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ConfigurarTeste.this,MainActivity.class);
                startActivity(i);
            }
        });
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

        irPara = findViewById(R.id.irPara);
    }
}
