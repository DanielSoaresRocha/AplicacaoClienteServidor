package com.example.bolsista.novatentativa.viewHolders;

import android.view.View;
import android.widget.TextView;

import com.example.bolsista.novatentativa.R;

import androidx.recyclerview.widget.RecyclerView;

public class CavaloViewHolder extends RecyclerView.ViewHolder{

    public final TextView nomeCavalo;
    public final TextView detalhesCavalo;

    public CavaloViewHolder(View v) {
        super(v);
        this.nomeCavalo = (TextView) v.findViewById(R.id.nomeCavalo);
        this.detalhesCavalo = (TextView) v.findViewById(R.id.detalhesCavalo);
    }
}
