package com.example.bolsista.novatentativa.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bolsista.novatentativa.R;

public class SessaoViewHolder extends RecyclerView.ViewHolder{
    public final TextView nomeSessao;
    public final TextView dataSessao;
    public final TextView porcentagemAcerto;
    public final TextView experimentador;


    public SessaoViewHolder(View v) {
        super(v);
        nomeSessao = v.findViewById(R.id.nomeSessao);
        dataSessao = v.findViewById(R.id.dataSessao);
        porcentagemAcerto = v.findViewById(R.id.porcentagemAcerto);
        experimentador = v.findViewById(R.id.experimentador);
    }
}