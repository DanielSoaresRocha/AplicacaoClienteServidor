package com.example.bolsista.novatentativa.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.modelo.Ensaio;
import com.example.bolsista.novatentativa.viewHolders.EnsaioViewHolder;

import java.text.DecimalFormat;
import java.util.List;

public class EnsaioAdapter extends RecyclerView.Adapter<EnsaioViewHolder> {
    private Context c;
    List<Ensaio> ensaios;

    DecimalFormat formato = new DecimalFormat("#.##");

    public EnsaioAdapter(Context c, List<Ensaio> ensaios) {
        this.c = c;
        this.ensaios = ensaios;
    }

    @NonNull
    @Override
    public EnsaioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.ensaio_inflater,parent,false);
        Log.i("ensaioAdapter", "listando ensaios...");
        return new EnsaioViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EnsaioViewHolder holder, int position) {
        Ensaio ensaioEscolhido = ensaios.get(position);

        holder.tempoAcertoText.setText(millisParaMinutos(ensaioEscolhido.getTempoAcerto()) + " minutos");
        holder.img1.setImageResource(ensaioEscolhido.getDesafio().getImg1());
        holder.imgCorreta.setImageResource(ensaioEscolhido.getDesafio().getImgCorreta());
        holder.img2.setImageResource(ensaioEscolhido.getDesafio().getImg2());
    }
    @Override
    public int getItemCount() {
        return ensaios.size();
    }

    private String millisParaMinutos(long tempoMillis) {
        double tempoEmMinutos = (double) (tempoMillis / 1000) / 60;

        return formato.format(tempoEmMinutos);
    }


}
