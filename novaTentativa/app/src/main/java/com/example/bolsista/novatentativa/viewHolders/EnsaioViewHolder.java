package com.example.bolsista.novatentativa.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bolsista.novatentativa.R;

public class EnsaioViewHolder extends RecyclerView.ViewHolder {

    public final TextView tempoAcertoText;
    public final ImageView img1, imgCorreta, img2;
    public final LinearLayout corBordaEnsaio;

    public EnsaioViewHolder(View v) {
        super(v);
        this.tempoAcertoText = (TextView) v.findViewById(R.id.tempoAcertoText);
        this.img1 = (ImageView) v.findViewById(R.id.img1);
        this.imgCorreta = (ImageView) v.findViewById(R.id.imgCorreta);
        this.img2 = (ImageView) v.findViewById(R.id.img2);
        this.corBordaEnsaio = (LinearLayout) v.findViewById(R.id.corBordaEnsaio);
    }
}
