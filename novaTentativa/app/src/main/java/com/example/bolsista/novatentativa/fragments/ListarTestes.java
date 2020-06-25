package com.example.bolsista.novatentativa.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.drakeet.materialdialog.MaterialDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bolsista.novatentativa.IniciarConfiguracao;
import com.example.bolsista.novatentativa.R;
import com.example.bolsista.novatentativa.adapters.TesteAdapter;
import com.example.bolsista.novatentativa.cadastros.ConfigurarTeste;
import com.example.bolsista.novatentativa.modelo.Configuracao;
import com.example.bolsista.novatentativa.modelo.Desafio;
import com.example.bolsista.novatentativa.modelo.Sessao;
import com.example.bolsista.novatentativa.recycleOnTouchLinesters.GenericOnItemTouch;
import com.example.bolsista.novatentativa.viewsModels.ListarViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/*
* colocar sessão atual para determinado cavalo
*/

public class ListarTestes extends Fragment {
    private ListarViewModel mViewModel;
    private View v;
    private Context contextoAtivity;

    private RecyclerView configuracaoRecycle;
    private ProgressBar progressBarConfig;
    private FloatingActionButton floatingActionButton;

    @SuppressLint("StaticFieldLeak")
    public static TesteAdapter adapter;

    //FireBase FireStore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usuarioRef;
    //FireBase autenth
    FirebaseAuth usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_listar_testes, container, false);
        return v;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ListarViewModel.class);
        // TODO: Use the ViewModel
        inicializar();
        listener();

        if(mViewModel.cavalos.getValue().size() == 0){ // se a lista estiver vazia
            //getConfiguracoesFireStore();// pegar do banco
            /*
            Preste atenção que a ordem das figuras está como nos testes de gabor, onde a figura do meio
            representa a img certa, e as laterais sendo a certa ou a errada
            */
            //DESAFIOS TESTE L1
            ArrayList<Desafio> desafiosL1 = new ArrayList<>();
            desafiosL1.add(new Desafio("1", R.drawable.branco, R.drawable.estrela2, R.drawable.estrela2));
            desafiosL1.add(new Desafio("2", R.drawable.estrela2, R.drawable.estrela2, R.drawable.branco));
            desafiosL1.add(new Desafio("3", R.drawable.branco, R.drawable.circulo,  R.drawable.circulo));
            desafiosL1.add(new Desafio("4", R.drawable.circulo, R.drawable.circulo,  R.drawable.branco));
            //DESAFIOS TESTE L2
            ArrayList<Desafio> desafiosL2 = new ArrayList<>();
            desafiosL2.add(new Desafio("5",R.drawable.circulo, R.drawable.estrela2, R.drawable.estrela2));
            desafiosL2.add(new Desafio("6",R.drawable.estrela2, R.drawable.estrela2, R.drawable.circulo));
            desafiosL2.add(new Desafio("7",R.drawable.estrela2, R.drawable.circulo, R.drawable.circulo));
            desafiosL2.add(new Desafio("8",R.drawable.circulo, R.drawable.circulo, R.drawable.estrela2));
            //DESAFIOS TESTE T1
            ArrayList<Desafio> desafiosT1 = new ArrayList<>();
            desafiosT1.add(new Desafio("10",R.drawable.triangulo, R.drawable.triangulo, R.drawable.retanguloo));
            desafiosT1.add(new Desafio("11",R.drawable.retanguloo, R.drawable.triangulo, R.drawable.triangulo));
            desafiosT1.add(new Desafio("12",R.drawable.triangulo, R.drawable.retanguloo, R.drawable.retanguloo));
            desafiosT1.add(new Desafio("13",R.drawable.retanguloo, R.drawable.retanguloo, R.drawable.triangulo));

            //PRE-TESTE
            ListarViewModel.addConfiguracao(new Configuracao("0", "Pré-teste", 0,
                    0, 0, R.raw.sucess, R.raw.error, new ArrayList<Desafio>(),
                    "Teste Gabor", 0, 0, new ArrayList<Sessao>(),
                    true, 0, 0, 0,
                    false));
            //TESTE L1
            ListarViewModel.addConfiguracao(new Configuracao("1", "Teste de aprendizagem L1",
                    5, 5, desafiosL1.size(), R.raw.sucess, R.raw.error,
                    desafiosL1, "Teste Gabor", 0, 2,
                    new ArrayList<Sessao>(), false, 20, 3,
                    85,false));
            //TESTE L2
            ListarViewModel.addConfiguracao(new Configuracao("2", "Teste de aprendizagem L2",
                    5, 5, desafiosL2.size(), R.raw.sucess, R.raw.error,
                    desafiosL2, "Teste Gabor", 0, 2,
                    new ArrayList<Sessao>(), false, 20, 3,
                    85,false));
            //TESTE T1
            ListarViewModel.addConfiguracao(new Configuracao("3", "Teste de transferência T1",
                    5, 5, desafiosT1.size(), R.raw.sucess, R.raw.error,
                    desafiosT1, "Teste Gabor", 0, 2,
                    new ArrayList<Sessao>(), false, 20, 3,
                    85,false));
            //TESTE T2
            ListarViewModel.addConfiguracao(new Configuracao("4", "Teste de transferência T2",
                    5, 5, desafiosT1.size(), R.raw.sucess, R.raw.error,
                    desafiosT1, "Teste Gabor", 0, 2,
                    new ArrayList<Sessao>(), false, 20, 3,
                    85,false));
            implementsRecycle();
            observerList();
        }else{// se não
            implementsRecycle();// apenas implemente o recycle
        }
    }

    private void listener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaConfiguração = new Intent(getContext(), ConfigurarTeste.class);
                startActivity(telaConfiguração);
            }
        });
    }

    private void getConfiguracoesFireStore() {
        db.collection("configuracoes")
                //.whereEqualTo("users", usuarioRef)//referencia do usuario que adicionou o cavalo
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Configuracao configuracao = document.toObject(Configuracao.class);
                                mViewModel.addConfiguracao(configuracao);
                                Log.i("DataBase-FireStore-get", "referencia de => ." +
                                        configuracao.getNome() + " = " +
                                        document.getDocumentReference("usuario").getId());
                            }
                            implementsRecycle();
                            observerList();
                        } else {
                            Log.i("DataBase-FireStore-get", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void implementsRecycle(){
        adapter = new TesteAdapter(contextoAtivity,mViewModel.configuracoes.getValue(),false);
        configuracaoRecycle.setAdapter(adapter);

        LinearLayoutManager layout = new LinearLayoutManager(contextoAtivity,LinearLayoutManager.VERTICAL,false);
        configuracaoRecycle.setLayoutManager(layout);

        configuracaoRecycle.addOnItemTouchListener(
                new GenericOnItemTouch(
                        contextoAtivity,
                        configuracaoRecycle,
                        new GenericOnItemTouch.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                IniciarConfiguracao.configuracaoSelecionada = mViewModel.configuracoes.getValue().get(position);
                                Toast.makeText(contextoAtivity,"Configuracao selecionada",
                                        Toast.LENGTH_SHORT).show();
                                Log.i("Teste", "onSingleTapUp2");
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onItemLongClick(View view, int position) {
                                View layout = LayoutInflater.from(contextoAtivity)
                                        .inflate(R.layout.configuracaoinformacao_inflater,null,false);

                                TextView detalhes = layout.findViewById(R.id.detalhesConfigInfo);
                                TextView intervalo1 = layout.findViewById(R.id.intervalo1ConfigInfo);
                                TextView intervalo2 = layout.findViewById(R.id.intervalo2ConfigInfo);
                                TextView qtdQuestoes = layout.findViewById(R.id.qtdQuestoesConfigInfo);

                                detalhes.setText(mViewModel.configuracoes.getValue().get(position)
                                        .getObservacoes());
                                intervalo1.setText(mViewModel.configuracoes.getValue().get(position)
                                        .getIntervalo1()+" segundos");
                                intervalo2.setText(mViewModel.configuracoes.getValue().get(position)
                                        .getIntervalo2()+" segundos");
                                qtdQuestoes.setText(mViewModel.configuracoes.getValue().get(position)
                                        .getQtdDesafios()+" questões");

                                MaterialDialog m = new MaterialDialog(contextoAtivity)
                                        .setContentView(layout)
                                        .setCanceledOnTouchOutside(true);

                                m.show();
                            }
                        })
        );

        progressBarConfig.setVisibility(View.GONE);
    }

    private void observerList() {
        mViewModel.configuracoes.observe(this, configuracoes ->{
            // update UI
            adapter.notifyDataSetChanged();
        });
    }

    private void inicializar() {
        configuracaoRecycle = v.findViewById(R.id.configuracaoRecycle);
        progressBarConfig = v.findViewById(R.id.progressBarConfig);
        floatingActionButton = v.findViewById(R.id.floatingActionButton);

        contextoAtivity = getActivity();

        usuario = FirebaseAuth.getInstance();
        try {
            usuarioRef = db.collection("users").document(usuario.getCurrentUser().getUid());
        }catch (java.lang.NullPointerException e){
            usuarioRef = db.collection("users").document("zl1hFltVOlJONAVUeIsY");
        }
    }
}
