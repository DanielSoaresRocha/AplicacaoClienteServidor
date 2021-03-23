package com.example.bolsista.novatentativa.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.ExperimentoAdapter;
import com.example.bolsista.novatentativa.cadastros.Gerenciar;
import com.example.bolsista.novatentativa.modelo.Equino;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.recycleOnTouchLinesters.GenericOnItemTouch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import me.drakeet.materialdialog.MaterialDialog;

public class ExperimentosGerenciar extends Fragment {
    private View v;
    private Context contextoAtivity;

    private Gerenciar gerenciar;

    private RecyclerView experimentosRecycleEdit;
    private ProgressBar progressBarExperimentosEdit;

    ExperimentoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_experimentos_gerenciar, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inicializar();
        getCavalosFireStore();
    }

    private void getCavalosFireStore() {
        gerenciar.db.collection("experimentos")
                //.whereEqualTo("users", usuarioRef)//referencia do usuario que adicionou o cavalo
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Experimento experimento = document.toObject(Experimento.class);
                                gerenciar.getExperimentos().add(experimento);
                            }
                            implementsRecycle();
                        } else {
                            Log.i("DataBase-FireStore-get", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void implementsRecycle(){
        adapter = new ExperimentoAdapter(contextoAtivity, gerenciar.getExperimentos());
        experimentosRecycleEdit.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextoAtivity,LinearLayoutManager.VERTICAL,false);
        experimentosRecycleEdit.setLayoutManager(layout);

        experimentosRecycleEdit.addOnItemTouchListener(
                new GenericOnItemTouch(
                        contextoAtivity,
                        experimentosRecycleEdit,
                        new GenericOnItemTouch.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                View layout = LayoutInflater.from(contextoAtivity)
                                        .inflate(R.layout.gerenciar_inflater,null,false);

                                MaterialDialog m = new MaterialDialog(contextoAtivity)
                                        .setContentView(layout)
                                        .setCanceledOnTouchOutside(true);

                                m.show();

                                ImageView excluir = layout.findViewById(R.id.deleteClick);
                                LinearLayout confirmDelete = layout.findViewById(R.id.confirmDelete);
                                LinearLayout linearEdit = layout.findViewById(R.id.linearEdit);
                                Button deleteEquino = layout.findViewById(R.id.deleteEquino);
                                TextView mensagemExcluir = layout.findViewById(R.id.mensagemExcluir);

                                linearEdit.setVisibility(View.GONE);

                                excluir.setOnClickListener(new View.OnClickListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onClick(View v) {
                                        mensagemExcluir.setText("Têm certeza que deseja excluir o experimento "+
                                                gerenciar.getExperimentos().get(position).getNome() + "?");

                                        confirmDelete.setVisibility(View.VISIBLE);
                                    }
                                });

                                deleteEquino.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deletarExperimento(gerenciar.getExperimentos().get(position));
                                        m.dismiss();
                                    }
                                });
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        })
        );

        progressBarExperimentosEdit.setVisibility(View.GONE);
    }

    private void deletarExperimento(Experimento experimento){
        gerenciar.db.collection("experimentos").document(experimento.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(contextoAtivity, "Experimento Deletado", Toast.LENGTH_SHORT).show();
                        gerenciar.getExperimentos().remove(experimento);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(contextoAtivity, "Falha ao deletar", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void inicializar(){
        contextoAtivity = v.getContext();
        gerenciar = (Gerenciar) getActivity();

        experimentosRecycleEdit = v.findViewById(R.id.experimentosRecycleEdit);
        progressBarExperimentosEdit = v.findViewById(R.id.progressBarExperimentosEdit);
    }

}