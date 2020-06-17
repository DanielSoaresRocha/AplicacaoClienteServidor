package com.example.bolsista.novatentativa.othersActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;

public class ExperimentoExpecifico extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experimento_expecifico);

        inicializar();
        pegarId();
    }

    private void pegarId() {
        Intent it = getIntent();
        int id = it.getIntExtra("idExperimento", 0);
        Toast.makeText(this, ExperimentosAndamento.experimentos2.get(id).getEquino(),
                Toast.LENGTH_SHORT).show();


    }

    private void inicializar() {
    }
}
