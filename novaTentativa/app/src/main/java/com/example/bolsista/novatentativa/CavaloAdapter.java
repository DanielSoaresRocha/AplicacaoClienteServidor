package com.example.bolsista.novatentativa;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CavaloAdapter extends RecyclerView.Adapter<CavaloViewHolder>{

    Context c;
    static List<Cavalo> cavalos;

    public CavaloAdapter(Context c, List<Cavalo> cavalos) {
        this.c = c;
        this.cavalos = cavalos;
    }

    @NonNull
    @Override
    public CavaloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.cavalo_inflater,parent,false);
        Log.i("cavaloAdapter", "listando...");
        return new CavaloViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CavaloViewHolder holder, int position) {
        Cavalo cavaloEscolhido = cavalos.get(position);
        System.out.println("=>>>>>>>>>>"+ cavaloEscolhido.getNome());
        holder.nomeCavalo.setText(cavaloEscolhido.getNome());
        holder.detalhesCavalo.setText(cavaloEscolhido.getDetalhes());
    }

    @Override
    public int getItemCount() {
        return cavalos.size();
    }

}
