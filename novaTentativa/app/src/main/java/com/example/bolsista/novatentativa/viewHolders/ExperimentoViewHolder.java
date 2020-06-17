package com.example.bolsista.novatentativa.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bolsista.novatentativa.R;

public class ExperimentoViewHolder extends RecyclerView.ViewHolder{

    public final TextView nomeExperimento2;
    public final TextView nomeEquino;
    public final TextView dataInicioE;

    public ExperimentoViewHolder(View v) {
        super(v);
        this.nomeExperimento2 = v.findViewById(R.id.nomeExperimento2);
        this.nomeEquino = v.findViewById(R.id.nomeEquino);
        this.dataInicioE = v.findViewById(R.id.dataInicioE);
    }
}
