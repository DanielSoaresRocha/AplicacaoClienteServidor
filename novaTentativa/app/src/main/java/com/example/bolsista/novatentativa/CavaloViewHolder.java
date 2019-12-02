package com.example.bolsista.novatentativa;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CavaloViewHolder extends RecyclerView.ViewHolder{

    final TextView nomeCavalo;
    final TextView detalhesCavalo;

    public CavaloViewHolder(View v) {
        super(v);
        this.nomeCavalo = (TextView) v.findViewById(R.id.nomeCavalo);
        this.detalhesCavalo = (TextView) v.findViewById(R.id.detalhesCavalo);
    }
}
