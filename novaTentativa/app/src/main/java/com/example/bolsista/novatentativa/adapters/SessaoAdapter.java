package com.example.bolsista.novatentativa.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Configuracao;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.example.bolsista.novatentativa.viewHolders.ConfiguracaoViewHolder;
import com.example.bolsista.novatentativa.viewHolders.SessaoViewHolder;

import java.util.Calendar;
import java.util.List;

public class SessaoAdapter extends RecyclerView.Adapter<SessaoViewHolder> {
    Context c;
    private List<Sessao> sessoes;

    public SessaoAdapter(Context c, List<Sessao> sessoes) {
        this.c = c;
        this.sessoes = sessoes;
    }

    @NonNull
    @Override
    public SessaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.sessao_inflater,parent,false);
        Log.i("configuracaoAdapter", "listando config...");
        return new SessaoViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SessaoViewHolder holder, int position) {
        Sessao sessaoEscolhida = sessoes.get(position);

        holder.nomeSessao.setText(sessaoEscolhida.getNome());
        holder.experimentador.setText(sessaoEscolhida.getExperimentador().getNome());
        holder.porcentagemAcerto.setText(sessaoEscolhida.getTaxaAcerto()+"%");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sessaoEscolhida.getData());
        holder.dataSessao.setText(calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)
                +"/"+calendar.get(Calendar.YEAR));
    }

    @Override
    public int getItemCount() {
        return sessoes.size();
    }
}