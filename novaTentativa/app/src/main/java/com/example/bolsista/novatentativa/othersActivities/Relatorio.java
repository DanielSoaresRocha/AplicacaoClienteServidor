package com.example.bolsista.novatentativa.othersActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Experimento;

public class Relatorio extends AppCompatActivity {
    private Experimento experimento;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        
        pegarExperimento();
    }

    private void pegarExperimento() {
        experimento = (Experimento) getIntent().getSerializableExtra("experimento");
    }
}