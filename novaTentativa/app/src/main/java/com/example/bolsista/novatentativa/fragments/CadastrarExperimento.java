package com.example.bolsista.novatentativa.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.IniciarConfiguracao;
import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.arquitetura.Servidor;
import com.example.bolsista.novatentativa.modelo.Experimento;
import com.example.bolsista.novatentativa.viewsModels.ListarViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class CadastrarExperimento extends Fragment {
    private ListarViewModel mViewModel;
    private View v;
    private Context contextoAtivity;

    private Button finalizarExperimento, btnTextExp;
    private EditText nomeExperimento, descricaoExperimento;
    @SuppressLint("StaticFieldLeak")
    private static TextView numExpCavalo;

    //FireBase FireStore
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usuarioRef;
    //FireBase autenth
    FirebaseAuth usuario;

    static int numberExperiments = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cadastrar_experimento, container, false);
        return v;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ListarViewModel.class);
        // TODO: Use the ViewModel
        inicializar();
        listener();
    }

    private void listener() {
        finalizarExperimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nomeExperimento.getText().toString().length() > 0){
                    instanciarExperimento();
                }
            }
        });

        btnTextExp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                numExpCavalo.setText(""+numberExperiments);
            }
        });
    }

    private void instanciarExperimento(){
        DocumentReference configuracaoRef = db.collection("configuracoes")
                .document(IniciarConfiguracao.configuracaoSelecionada.getId());
        DocumentReference equinoRef = db.collection("equinos")
                .document(IniciarConfiguracao.cavaloSelecionado.getId());

        IniciarConfiguracao.experimento = new Experimento("", configuracaoRef, usuarioRef, equinoRef, new Date(),
                descricaoExperimento.getText().toString(), nomeExperimento.getText().toString(), 0);

        addExperimentoToFireBase();
        ListarViewModel.addExperimento(IniciarConfiguracao.experimento);
    }

    private void addExperimentoToFireBase(){
        db.collection("experimentos")
                .add(IniciarConfiguracao.experimento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentReference.update("id",documentReference.getId());//adiciona ao campo id o id gerado pelo firebase
                        IniciarConfiguracao.experimento.setId(documentReference.getId());
                        Toast.makeText(contextoAtivity, "Experimento adicionado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DataBase-FireStore-add", "Error adding document", e);
                    }
                });
        iniciarServidor();
    }

    public static void verififyNumberExperiments(String id){
        System.out.println("=>>>> ID = "+ id);
        DocumentReference equinoRef = db.collection("equinos")
                .document(id);

        db.collection("experimentos")
                .whereEqualTo("equino", equinoRef)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("entrou "+numberExperiments+" vezes", document.getId()
                                        + " => " + document.getData());
                                numberExperiments++;
                            }
                        } else {
                            Log.d("notOK", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void iniciarServidor() {
        getActivity().finish();
        Intent iniciarServidor = new Intent(contextoAtivity, Servidor.class);
        startActivity(iniciarServidor);
    }


    private void inicializar() {
        finalizarExperimento = v.findViewById(R.id.finalizarExperimento);
        nomeExperimento = v.findViewById(R.id.nomeExperimento);
        descricaoExperimento = v.findViewById(R.id.descricaoExperimento);
        numExpCavalo = v.findViewById(R.id.numExpCavalo);
        btnTextExp = v.findViewById(R.id.btnTextExp);

        contextoAtivity = getActivity();

        usuario = FirebaseAuth.getInstance();
        try {
            usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());
        }catch (java.lang.NullPointerException e){
            usuarioRef = db.collection("users").document("zl1hFltVOlJONAVUeIsY");
        }
    }
}
