package com.example.bolsista.novatentativa.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bolsista.novatentativa.R;

import androidx.recyclerview.widget.RecyclerView;

public class ResultadoViewHolder extends RecyclerView.ViewHolder{
    public ImageView imgCorreta, imgErrada;
    public TextView qtdErros, tempo;


    public ResultadoViewHolder(View v) {
        super(v);
        this.imgCorreta = v.findViewById(R.id.imgCorreta);
        this.imgErrada = v.findViewById(R.id.imgErrada);
        this.qtdErros = v.findViewById(R.id.qtdErros);
        this.tempo = v.findViewById(R.id.tempo);
    }
}
