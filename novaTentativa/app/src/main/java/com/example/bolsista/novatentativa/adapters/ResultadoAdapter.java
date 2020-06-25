package com.example.bolsista.novatentativa.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Desafio;
import com.example.bolsista.novatentativa.viewHolders.ResultadoViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResultadoAdapter extends RecyclerView.Adapter<ResultadoViewHolder>{
    Context c;
    List<Desafio> desafios;

    public ResultadoAdapter(Context c, List<Desafio> desafios) {
        this.c = c;
        this.desafios = desafios;
    }

    @NonNull
    @Override
    public ResultadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.resultado_inflater,parent,false);
        Log.i("cavaloAdapter", "listando...");
        return new ResultadoViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ResultadoViewHolder holder, int position) {
        Desafio desafioEscolhido = desafios.get(position);
        holder.imgCorreta.setImageResource(desafioEscolhido.getImgCorreta());
        //holder.imgErrada.setImageResource(desafioEscolhido.getImgErrada());
        //holder.qtdErros.setText(Integer.toString(desafioEscolhido.getQtdErros()));
        //holder.tempo.setText(Long.toString(desafioEscolhido.getTempoAcerto()));
    }

    @Override
    public int getItemCount() {
        return desafios.size();
    }
}
