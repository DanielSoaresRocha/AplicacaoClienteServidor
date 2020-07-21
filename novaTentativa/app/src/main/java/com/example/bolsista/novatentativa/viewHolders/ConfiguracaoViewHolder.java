package com.example.bolsista.novatentativa.viewHolders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.bolsista.novatentativa.R;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ConfiguracaoViewHolder extends RecyclerView.ViewHolder{

    public final TextView nomeConfig;
    public final TextView detalhesConfig;
    public final CheckBox testCheckBox;
    public final CardView testCardView;

    public ConfiguracaoViewHolder(View v) {
        super(v);
        this.nomeConfig = (TextView) v.findViewById(R.id.nomeConfig);
        this.detalhesConfig = (TextView) v.findViewById(R.id.detalhesConfig);
        this.testCheckBox = v.findViewById(R.id.testCheckBox);
        this.testCardView = v.findViewById(R.id.testCardView);
    }
}
