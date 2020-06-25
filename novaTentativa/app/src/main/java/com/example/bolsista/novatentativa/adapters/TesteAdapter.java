package com.example.bolsista.novatentativa.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Teste;
import com.example.bolsista.novatentativa.viewHolders.ConfiguracaoViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TesteAdapter extends RecyclerView.Adapter<ConfiguracaoViewHolder> {
    private Context c;
    static List<Teste> configuracoes;
    private boolean changeColor;

    public TesteAdapter(Context c, List<Teste> configuracoes, Boolean changeColor) {
        this.c = c;
        this.configuracoes = configuracoes;
        this.changeColor = changeColor;
    }

    @NonNull
    @Override
    public ConfiguracaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.teste_inflater,parent,false);
        Log.i("configuracaoAdapter", "listando config...");
        return new ConfiguracaoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfiguracaoViewHolder holder, int position) {
        Teste configEscolhida = configuracoes.get(position);
        holder.nomeConfig.setText(configEscolhida.getNome());
        holder.detalhesConfig.setText(configEscolhida.getObservacoes());
        holder.testCheckBox.setChecked(true);

        //mudar background
        if(configEscolhida.isCompleto()) {
            holder.testCheckBox.setVisibility(View.GONE);
            if (configEscolhida.isCompleto())
                holder.testCardView.setBackgroundResource(R.drawable.fundo_verde);
        }
    }

    @Override
    public int getItemCount() {
        return configuracoes.size();
    }
}
