package com.example.bolsista.novatentativa.viewHolders;

import android.view.View;
import android.widget.TextView;

import com.example.bolsista.novatentativa.R;

import androidx.recyclerview.widget.RecyclerView;

public class ConfiguracaoViewHolder extends RecyclerView.ViewHolder{

    public final TextView comeConfig;
    public final TextView detalhesConfig;

    public ConfiguracaoViewHolder(View v) {
        super(v);
        this.comeConfig = (TextView) v.findViewById(R.id.nomeConfig);
        this.detalhesConfig = (TextView) v.findViewById(R.id.detalhesConfig);
    }
}
