package com.example.bolsista.novatentativa.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.euicc.EuiccInfo;
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

import com.example.bolsista.novatentativa.NovoExperimento;
import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.EquinoAdapter;
import com.example.bolsista.novatentativa.cadastros.CadastrarEquino;
import com.example.bolsista.novatentativa.cadastros.Gerenciar;
import com.example.bolsista.novatentativa.modelo.Equino;
import com.example.bolsista.novatentativa.othersActivities.SessaoExpecifica;
import com.example.bolsista.novatentativa.recycleOnTouchLinesters.GenericOnItemTouch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.GregorianCalendar;

import me.drakeet.materialdialog.MaterialDialog;

public class EquinosGerenciar extends Fragment {
    private View v;
    private Context contextoAtivity;

    private Gerenciar gerenciar;

    private RecyclerView equinosRecycleEdit;
    private ProgressBar progressBarEquinosEdit;

    EquinoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_equinos_edit_delete, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        inicializar();
        getCavalosFireStore();
    }

    private void getCavalosFireStore() {
        gerenciar.db.collection("equinos")
                //.whereEqualTo("users", usuarioRef)//referencia do usuario que adicionou o cavalo
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Equino equino = document.toObject(Equino.class);
                                gerenciar.getEquinos().add(equino);
                            }
                            implementsRecycle();
                        } else {
                            Log.i("DataBase-FireStore-get", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void implementsRecycle(){
        adapter = new EquinoAdapter(contextoAtivity, gerenciar.getEquinos());
        equinosRecycleEdit.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextoAtivity,LinearLayoutManager.VERTICAL,false);
        equinosRecycleEdit.setLayoutManager(layout);

        equinosRecycleEdit.addOnItemTouchListener(
                new GenericOnItemTouch(
                        contextoAtivity,
                        equinosRecycleEdit,
                        new GenericOnItemTouch.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                View layout = LayoutInflater.from(contextoAtivity)
                                        .inflate(R.layout.gerenciar_inflater,null,false);

                                MaterialDialog m = new MaterialDialog(contextoAtivity)
                                        .setContentView(layout)
                                        .setCanceledOnTouchOutside(true);

                                m.show();

                                ImageView editar = layout.findViewById(R.id.editClick);
                                ImageView excluir = layout.findViewById(R.id.deleteClick);
                                LinearLayout confirmDelete = layout.findViewById(R.id.confirmDelete);
                                Button deleteEquino = layout.findViewById(R.id.deleteEquino);
                                TextView mensagemExcluir = layout.findViewById(R.id.mensagemExcluir);

                                editar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent it = new Intent(contextoAtivity, CadastrarEquino.class);
                                        it.putExtra("equino", gerenciar.getEquinos().get(position));
                                        startActivity(it);
                                        getActivity().finish();
                                    }
                                });

                                excluir.setOnClickListener(new View.OnClickListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onClick(View v) {
                                        mensagemExcluir.setText("Têm certeza que deseja excluir o equino "+
                                                gerenciar.getEquinos().get(position).getNome() + "?");

                                        confirmDelete.setVisibility(View.VISIBLE);
                                    }
                                });

                                deleteEquino.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deletarEquino(gerenciar.getEquinos().get(position));
                                        m.dismiss();
                                    }
                                });
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onItemLongClick(View view, int position) {
                                View layout = LayoutInflater.from(contextoAtivity)
                                        .inflate(R.layout.cavaloinformacao_inflater,null,false);

                                TextView nomeCavalo = layout.findViewById(R.id.nomeEquinoInfo);
                                TextView detalhes = layout.findViewById(R.id.detalhesEquinoInfo);
                                TextView idade = layout.findViewById(R.id.idadeEquinoInfo);
                                TextView raca = layout.findViewById(R.id.racaEquinoInfo);

                                nomeCavalo.setText(gerenciar.getEquinos().get(position).getNome());
                                detalhes.setText(gerenciar.getEquinos().get(position).getObservacoes());
                                idade.setText(calculaIdade(gerenciar.getEquinos()
                                        .get(position).getDataNascimento()) + " anos");
                                raca.setText(gerenciar.getEquinos().get(position).getRaca());

                                MaterialDialog m = new MaterialDialog(contextoAtivity)
                                        .setContentView(layout)
                                        .setCanceledOnTouchOutside(true);

                                m.show();
                            }
                        })
        );

        progressBarEquinosEdit.setVisibility(View.GONE);
    }

    private void deletarEquino(Equino equino){
        gerenciar.db.collection("equinos").document(equino.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(contextoAtivity, "Equino Deletado", Toast.LENGTH_SHORT).show();
                        gerenciar.getEquinos().remove(equino);
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

    // Retorna o calculo da idade atual a partir de uma data
    // Código disponível em https://www.devmedia.com.br/calcule-a-idade-corretamente-em-java/4729
    public static int calculaIdade(java.util.Date dataNasc){
        Calendar dateOfBirth = new GregorianCalendar();
        dateOfBirth.setTime(dataNasc);
        // Cria um objeto calendar com a data atual
        Calendar today = Calendar.getInstance();
        // Obtém a idade baseado no ano
        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
        dateOfBirth.add(Calendar.YEAR, age);
        //se a data de hoje é antes da data de Nascimento, então diminui 1(um)
        if (today.before(dateOfBirth)) {
            age--;
        }
        return age;
    }

    private void inicializar(){
        contextoAtivity = v.getContext();
        gerenciar = (Gerenciar) getActivity();

        equinosRecycleEdit = v.findViewById(R.id.equinosRecycleEdit);
        progressBarEquinosEdit = v.findViewById(R.id.progressBarEquinosEdit);
    }

}