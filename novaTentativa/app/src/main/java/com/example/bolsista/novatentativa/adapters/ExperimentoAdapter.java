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
import com.example.bolsista.novatentativa.modelo.Experimento2;
import com.example.bolsista.novatentativa.viewHolders.ExperimentoViewHolder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ExperimentoAdapter extends RecyclerView.Adapter<ExperimentoViewHolder> {
    Context c;
    private List<Experimento2> experimentos;

    public ExperimentoAdapter(Context c, List<Experimento2> experimentos) {
        this.c = c;
        this.experimentos = experimentos;
    }

    @NonNull
    @Override
    public ExperimentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.experimento_inflater,parent,false);
        Log.i("ExperimentoAdapter", "listando...");
        return new ExperimentoViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExperimentoViewHolder holder, int position) {
        Experimento2 cavaloEscolhido = experimentos.get(position);
        holder.nomeExperimento2.setText(cavaloEscolhido.getNome());
        holder.nomeEquino.setText(cavaloEscolhido.getEquino());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cavaloEscolhido.getDataInicio());
        holder.dataInicioE.setText(calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)
        +"/"+calendar.get(Calendar.YEAR));


    }

    @Override
    public int getItemCount() {
        return experimentos.size();
    }
}
