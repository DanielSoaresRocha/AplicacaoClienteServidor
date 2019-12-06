package com.example.bolsista.novatentativa.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Configuracao;
import com.example.bolsista.novatentativa.viewHolders.ConfiguracaoViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ConfiguracaoAdapter extends RecyclerView.Adapter<ConfiguracaoViewHolder> {
    Context c;
    static List<Configuracao> configuracoes;

    public ConfiguracaoAdapter(Context c, List<Configuracao> configuracoes) {
        this.c = c;
        this.configuracoes = configuracoes;
    }

    @NonNull
    @Override
    public ConfiguracaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.configuracao_inflater,parent,false);
        Log.i("configuracaoAdapter", "listando config...");
        return new ConfiguracaoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfiguracaoViewHolder holder, int position) {
        Configuracao configEscolhida = configuracoes.get(position);
        holder.comeConfig.setText(configEscolhida.getNome());
        holder.detalhesConfig.setText(configEscolhida.getDetalhes());
    }

    @Override
    public int getItemCount() {
        return configuracoes.size();
    }
}
