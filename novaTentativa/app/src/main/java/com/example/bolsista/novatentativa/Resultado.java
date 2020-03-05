package com.example.bolsista.novatentativa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.bolsista.novatentativa.adapters.ResultadoAdapter;

public class Resultado extends AppCompatActivity {
    private RecyclerView desafiosRecycle;
    private ResultadoAdapter adapter;

    private Context contextoAtivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        inicializar();
        implementsRecycle();
    }

    private void implementsRecycle() {
        adapter = new ResultadoAdapter(contextoAtivity,Jogar.desafios);
        desafiosRecycle.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextoAtivity,LinearLayoutManager.VERTICAL,false);
        desafiosRecycle.setLayoutManager(layout);
    }

    private void inicializar() {
        desafiosRecycle = findViewById(R.id.desafiosRecycle);
        contextoAtivity = this;
    }
}
