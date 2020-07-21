package com.example.bolsista.novatentativa.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Equino;
import com.example.bolsista.novatentativa.viewHolders.EquinoViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EquinoAdapter extends RecyclerView.Adapter<EquinoViewHolder>{

    Context c;
    static List<Equino> equinos;

    public EquinoAdapter(Context c, List<Equino> equinos) {
        this.c = c;
        this.equinos = equinos;
    }

    @NonNull
    @Override
    public EquinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.cavalo_inflater,parent,false);
        Log.i("cavaloAdapter", "listando...");
        return new EquinoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EquinoViewHolder holder, int position) {
        Equino equinoEscolhido = equinos.get(position);
        holder.nomeCavalo.setText(equinoEscolhido.getNome());
        holder.detalhesCavalo.setText(equinoEscolhido.getRaca());
    }

    @Override
    public int getItemCount() {
        return equinos.size();
    }

}